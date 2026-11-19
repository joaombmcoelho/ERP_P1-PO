#!/bin/zsh

error_exit() {
    echo "Error: $1" >&2
    echo "Usage: $0 DELIVERY" >&2
    echo "          DELIVERY is one of 'e0', 'ei', or 'ef'" >&2
    exit 1
}

[[ $# -ne 1 ]] && error_exit "argument expected"

DELIVERY=$1

# code analysis (tools/) needs python3 with tree-sitter and the Java grammar
# (loading javaast checks both)
ANALYSIS=no
if [ "$DELIVERY" != "e0" ]; then
  if python3 -c 'import sys; sys.path.insert(0, "tools"); import javaast' 2>&1; then
    ANALYSIS=yes
  else
    echo "Code analysis unavailable: cannot load tree-sitter/Java grammar." >&2
  fi
fi

for group in checked-out/[0-9]??; do
  GROUP=`print -f "%.3d" $group:t`
  ./test-group.sh $DELIVERY $GROUP < /dev/null
  # append the code analysis to the results file (delivered projects only)
  RESFILE=logs/$DELIVERY/$GROUP.res.html
  if [ "$ANALYSIS" = "yes" ] && [ -f $RESFILE ] && ! /usr/bin/grep -q "^!!! Bad project" $RESFILE; then
    echo "#################### CODE ANALYSIS ####################" >> $RESFILE
    echo                                                           >> $RESFILE
    ANALYSIS_OUT=$(python3 tools/analyze_projects.py --single checked-out/$GROUP 2>&1)
    if [ $? -eq 0 ]; then
      print -r -- "$ANALYSIS_OUT"                                  >> $RESFILE
    else
      echo "Code analysis failed (internal error)."                >> $RESFILE
      print -r -- "Code analysis failed for $GROUP:" "$ANALYSIS_OUT" >&2
    fi
    echo                                                           >> $RESFILE
  fi
  # clean unnecessary files
  /bin/rm -f logs/$DELIVERY/*.out 2>/dev/null
  git add logs/$DELIVERY/$GROUP.res.html logs/$DELIVERY/$GROUP.log.html || true
  git commit -am "Updated test results for group $GROUP."
done
