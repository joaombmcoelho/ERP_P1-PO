#!/bin/zsh

error_exit() {
    echo "Error: $1" >&2
    echo "Usage: $0 DELIVERY" >&2
    echo "          DELIVERY is one of 'e0', 'ei', or 'ef'" >&2
    exit 1
}

[[ $# -ne 1 ]] && error_exit "argument expected"

# one of "e0", "ei", "ef"
DELIVERY=$1
LOGSDIR=logs/$DELIVERY

for group in checked-out/???; do
  GROUP=`print -f "%.3d" $group:t`
  RESFILE=$LOGSDIR/$GROUP.res.html
  LOGFILE=$LOGSDIR/$GROUP.log.html

  RES=$(/usr/bin/grep -E "^!!! Bad project" $RESFILE)
  if [ "$RES" != "" ]; then
    echo -n "* $GROUP [res]($RESFILE) [log]($LOGFILE) " >> GLOBAL-REPORT.md
    echo "❌ **NO DELIVERY**"                           >> GLOBAL-REPORT.md
    continue
  fi

  if [ "$DELIVERY" = "e0" ]; then

    RES=$(/usr/bin/grep -F "!! uml" $RESFILE | awk -F/ '{print $2}' | awk -F: '{print $1}')
    if [ "$RES" != "" ]; then
      MISSING_FILES=$(/usr/bin/grep -E -e "!! uml" $RESFILE | sed -e 's=!! uml/==g' | sed -e 's/ invalid or empty\.//g' | tr '\n' ' ' | sed -e 's/ *$//g' )
      echo -n "* $GROUP [res]($RESFILE) [log]($LOGFILE) " >> GLOBAL-REPORT.md
      if [[ $MISSING_FILES == *core* ]]; then
          echo "❌ **UML MISSING: $MISSING_FILES**" >> GLOBAL-REPORT.md
      else
          echo "⚠ **UML MISSING: $MISSING_FILES**" >> GLOBAL-REPORT.md
      fi
      continue
    fi
  
    RES=$(/usr/bin/grep -F "generated" $RESFILE)
    if [ "$RES" != "" ]; then
      GENERATED_JAR1=$(/usr/bin/grep -F "generated" $RESFILE | /usr/bin/grep -F core | sed -e "s=^[^']*'==g" | sed -e "s/'.*$//g")
      GENERATED_JAR2=$(/usr/bin/grep -F "generated" $RESFILE | /usr/bin/grep -F app  | sed -e "s=^[^']*'==g" | sed -e "s/'.*$//g")
      echo -n "* $GROUP [res]($RESFILE) [log]($LOGFILE) " >> GLOBAL-REPORT.md
      echo "✅ **UML PRESENT. BUILD SUCCESSFUL.**"        >> GLOBAL-REPORT.md
      continue
    fi
  
  fi

  # patterns are anchored: the code analysis appended to the results file
  # may contain these words (e.g. in method names or comments)
  RES=$(/usr/bin/grep -E "^-- successfully completed build" $RESFILE)
  if [ "$RES" != "" ]; then
    PC=$(/usr/bin/grep -E "^Total: " $RESFILE | awk '{print $2}' | sed -e 's/\.\([0-9]\)\([0-9]\)[0-9]*/.\1\2/g')
    echo -n "* $GROUP [res]($RESFILE) [log]($LOGFILE) " >> GLOBAL-REPORT.md
    echo "✅ **SUCCESS: $PC%**"                         >> GLOBAL-REPORT.md
    continue
  fi

  # default
  echo -n "* $GROUP [res]($RESFILE) [log]($LOGFILE) "   >> GLOBAL-REPORT.md

  if [ "$DELIVERY" = "e0" ]; then
    echo "⚠ **UML PRESENT. BUILD FAILED.**"             >> GLOBAL-REPORT.md
  else
    echo "⚠ **BUILD FAILED.**"                          >> GLOBAL-REPORT.md
  fi

done

#--------------------------------------------------------------------------
# global code analysis (delivered projects only), kept under version control

ANALYSIS_REPORT=CODE-ANALYSIS.md
if [ "$DELIVERY" != "e0" ] && python3 -c 'import sys; sys.path.insert(0, "tools"); import javaast' 2>/dev/null; then
  DELIVERED=()
  for group in checked-out/???; do
    GROUP=`print -f "%.3d" $group:t`
    RESFILE=$LOGSDIR/$GROUP.res.html
    [ -f $RESFILE ] && ! /usr/bin/grep -q "^!!! Bad project" $RESFILE && DELIVERED+=($GROUP)
  done
  if [ ${#DELIVERED[@]} -gt 0 ]; then
    if python3 tools/analyze_projects.py -q checked-out --only $DELIVERED \
         --link "$LOGSDIR/{project}.res.html" -o $ANALYSIS_REPORT; then
      git add $ANALYSIS_REPORT || true
    else
      echo "Code analysis failed: $ANALYSIS_REPORT not updated." >&2
    fi
  fi
else
  echo "Code analysis skipped (delivery $DELIVERY, or tree-sitter/Java grammar unavailable)." >&2
fi
