#!/usr/bin/env bash
# run-tests.sh — corre os testes automáticos (auto-tests/*.in) do projeto ERP.
# Vive em <projeto>/tests/ e corre-se de qualquer sítio: ./tests/run-tests.sh
# (a raiz do projeto é a pasta acima, onde estão erp-core, erp-app, PO_UILIB, Makefile).
#
# Uso:  ./tests/run-tests.sh [opções] [filtro ...]
#   filtro        prefixo/substring do nome do teste (ex.: A-02  ou  A-13-05)
#   -v            mostrar input/esperado/obtido também dos testes que passam
#   -q            só o resumo (sem detalhes dos que falham)
#   -s            comparação estrita (distingue maiúsculas/minúsculas; ignora só espaços)
#   -n            não compilar (usa os .jar já existentes)
#   -x            parar no primeiro teste que falhar
#   -t DIR        pasta dos testes (omissão: tests/auto-tests)
#   -j JAR        caminho do po-uilib.jar (omissão: procura automaticamente)
#   -h            ajuda
#
# Por omissão a comparação é igual à da avaliação oficial: ignora espaços,
# mudanças de linha e maiúsculas/minúsculas (diff -iwub -B).

set -u
CALLER_PWD="$PWD"
HERE="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"   # pasta tests/
ROOT="$(cd "$HERE/.." && pwd)"                         # pasta base do projeto
cd "$ROOT" || exit 1

VERBOSE=0; QUIET=0; STRICT=0; BUILD=1; STOP=0
TESTS_DIR="$HERE/auto-tests"; UILIB=""; FILTERS=()

# caminhos relativos passados em -t/-j são relativos a onde o comando foi corrido
abspath() { case "$1" in /*) echo "$1" ;; *) echo "$CALLER_PWD/$1" ;; esac; }

while [ $# -gt 0 ]; do
  case "$1" in
    -v) VERBOSE=1 ;; -q) QUIET=1 ;; -s) STRICT=1 ;; -n) BUILD=0 ;; -x) STOP=1 ;;
    -t) TESTS_DIR="$(abspath "$2")"; shift ;;
    -j) UILIB="$(abspath "$2")"; shift ;;
    -h|--help) sed -n '2,20p' "$0" | sed 's/^# \{0,1\}//'; exit 0 ;;
    -*) echo "Opção desconhecida: $1 (usa -h)"; exit 2 ;;
    *) FILTERS+=("$1") ;;
  esac
  shift
done

# ---- cores (só se for terminal) ----
if [ -t 1 ]; then
  R=$'\e[31m'; G=$'\e[32m'; Y=$'\e[33m'; B=$'\e[34m'; D=$'\e[2m'; N=$'\e[0m'; BD=$'\e[1m'
else R=;G=;Y=;B=;D=;N=;BD=; fi

# ---- localizar po-uilib.jar ----
if [ -z "$UILIB" ]; then
  for c in "${PO_UILIB:-}" /usr/share/java/po-uilib.jar "$ROOT"/PO_UILIB/po-uilib.jar \
           "$ROOT"/PO_UILIB/*/po-uilib.jar "$ROOT"/po-uilib.jar; do
    [ -n "$c" ] && [ -f "$c" ] && { UILIB="$c"; break; }
  done
  [ -z "$UILIB" ] && UILIB="$(find "$ROOT" /usr/share/java /opt -iname 'po-uilib*.jar' 2>/dev/null | head -1)"
fi
[ -f "$UILIB" ] || { echo "${R}Não encontrei o po-uilib.jar. Usa:  ./tests/run-tests.sh -j /caminho/po-uilib.jar${N}"; exit 2; }

[ -d "$TESTS_DIR" ] || { echo "${R}Pasta de testes não encontrada: $TESTS_DIR (usa -t DIR)${N}"; exit 2; }
EXP_DIR="$TESTS_DIR/expected"

# ---- compilar ----
if [ $BUILD -eq 1 ]; then
  echo "${BD}== A compilar (make) ==${N}"
  if ! make JAVADIR="$(dirname "$UILIB")" > "$HERE/build.log" 2>&1; then
    echo "${R}Compilação falhou. Últimas linhas (tests/build.log tem tudo):${N}"; tail -40 "$HERE/build.log"; exit 1
  fi
  echo "${G}Compilação OK${N}"
fi
CORE="$ROOT/erp-core/erp-core.jar"; APP="$ROOT/erp-app/erp-app.jar"
for j in "$CORE" "$APP"; do [ -f "$j" ] || { echo "${R}Falta $j${N}"; exit 1; }; done
CP="$UILIB:$CORE:$APP"

# ---- pasta de trabalho (os testes escrevem .dat e alguns dependem de outros) ----
WORK="$HERE/.test-work"; rm -rf "$WORK"; mkdir -p "$WORK"
RESULTS="$HERE/test-results"; rm -rf "$RESULTS"; mkdir -p "$RESULTS"
cp "$TESTS_DIR"/*.in "$WORK"/ 2>/dev/null
cp "$TESTS_DIR"/*.import "$WORK"/ 2>/dev/null

norm() { if [ $STRICT -eq 1 ]; then tr '\n\t' '  ' < "$1" | sed -e 's/  */ /g' -e 's/^ //' -e 's/ $//'
         else tr '\n\t' '  ' < "$1" | sed -e 's/  */ /g' -e 's/^ //' -e 's/ $//' | tr 'A-Z' 'a-z'; fi; }

show_block() { # título, ficheiro
  echo "${B}--- $1 ---${N}"; sed 's/^/    /' "$2"; [ -n "$(tail -c1 "$2")" ] && echo; echo
}

PASS=0; FAIL=0; NOOUT=0; TOTAL=0; FAILED_LIST=()
shopt -s nullglob
for inf in "$TESTS_DIR"/*.in; do
  name="$(basename "$inf" .in)"
  if [ ${#FILTERS[@]} -gt 0 ]; then
    m=0; for f in "${FILTERS[@]}"; do [[ "$name" == *"$f"* ]] && m=1; done
    [ $m -eq 0 ] && continue
  fi
  TOTAL=$((TOTAL+1))
  exp="$EXP_DIR/$name.out"; hyp="$WORK/$name.outhyp"; err="$RESULTS/$name.stderr"
  rm -f "$hyp"

  props=(-Din="$name.in" -Dout="$name.outhyp")
  [ -f "$WORK/$name.import" ] && props=(-Dimport="$name.import" "${props[@]}")
  ( cd "$WORK" && timeout 10 java -cp "$CP" "${props[@]}" erp.app.App ) > /dev/null 2> "$err"
  rc=$?

  status=""
  if [ ! -f "$hyp" ]; then status="NOOUT"
  elif [ ! -f "$exp" ]; then status="NOEXP"
  elif [ "$(norm "$exp")" == "$(norm "$hyp")" ]; then status="PASS"
  else status="FAIL"; fi

  case "$status" in
    PASS) PASS=$((PASS+1)); printf "%s✔ PASS%s  %s\n" "$G" "$N" "$name" ;;
    FAIL) FAIL=$((FAIL+1)); FAILED_LIST+=("$name"); printf "%s✘ FAIL%s  %s\n" "$R" "$N" "$name" ;;
    NOOUT) NOOUT=$((NOOUT+1)); FAILED_LIST+=("$name"); printf "%s✘ SEM OUTPUT%s  %s (exit=%s)\n" "$R" "$N" "$name" "$rc" ;;
    NOEXP) FAIL=$((FAIL+1)); FAILED_LIST+=("$name"); printf "%s? SEM FICHEIRO ESPERADO%s  %s\n" "$Y" "$N" "$name" ;;
  esac

  # relatório em ficheiro (sempre)
  if [ -f "$hyp" ]; then cp "$hyp" "$RESULTS/$name.obtained"; fi
  [ -f "$exp" ] && diff -u "$exp" "$hyp" > "$RESULTS/$name.diff" 2>/dev/null
  [ -s "$err" ] || rm -f "$err"

  # detalhes no ecrã
  if [ $QUIET -eq 0 ] && { [ "$status" != "PASS" ] || [ $VERBOSE -eq 1 ]; }; then
    echo "${D}  ------------------------------------------------------------${N}"
    [ -f "$WORK/$name.import" ] && show_block "IMPORT ($name.import)" "$WORK/$name.import"
    show_block "INPUT ($name.in)" "$inf"
    [ -f "$exp" ] && show_block "ESPERADO" "$exp"
    if [ -f "$hyp" ]; then show_block "OBTIDO" "$hyp"
      if [ "$status" = "FAIL" ]; then
        echo "${B}--- DIFF (- esperado / + obtido) ---${N}"
        diff -u --label esperado --label obtido -B -w "$exp" "$hyp" | tail -n +3 | sed \
          -e "s/^-/${R}-/" -e "s/^+/${G}+/" -e "s/\$/${N}/" | sed 's/^/    /'
        echo
      fi
    fi
    if [ -s "$err" ]; then
      echo "${Y}--- STDERR / excepções Java ---${N}"; head -25 "$err" | sed 's/^/    /'; echo
    fi
    echo "${D}  ------------------------------------------------------------${N}"
  fi
  [ $STOP -eq 1 ] && [ "$status" != "PASS" ] && break
done

echo
echo "${BD}================ RESUMO ================${N}"
echo "Total: $TOTAL   ${G}Passaram: $PASS${N}   ${R}Falharam: $((FAIL+NOOUT))${N}"
if [ ${#FAILED_LIST[@]} -gt 0 ]; then
  echo "${R}Falhados:${N}"; for f in "${FAILED_LIST[@]}"; do echo "  - $f"; done
  echo "${D}Diffs e outputs guardados em: tests/test-results/<teste>.diff|.obtained|.stderr${N}"
fi
[ $TOTAL -gt 0 ] && printf "Nota (%% de testes): %s%%\n" "$(( PASS * 100 / TOTAL ))"
rm -rf "$WORK"
[ $((FAIL+NOOUT)) -eq 0 ]