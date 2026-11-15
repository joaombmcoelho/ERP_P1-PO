#!/usr/bin/env bash
# run_tests_yang.sh — corre os testes do Tiago Yang (tests/tests_tiago_yang/auto-tests).
# Vive em <projeto>/tests/ e corre-se de qualquer sítio (a raiz é a pasta acima, com erp-core, erp-app, Makefile):
#   ./tests/run_tests_yang.sh [-n] [filtro ...]
#   -n   não compilar (usa os .jar já existentes)
# Comparação como no script original do Yang: diff -u -b -B (distingue maiúsculas/minúsculas).
# Diffs dos que falham ficam em tests/tests_tiago_yang/myout/<teste>.diff

set -u
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"   # raiz do projeto (pasta acima de tests/)
HERE="$ROOT/tests/tests_tiago_yang"
cd "$ROOT" || exit 1

BUILD=1; FILTERS=()
for a in "$@"; do
  case "$a" in
    -n) BUILD=0 ;;
    -h|--help) sed -n '2,7p' "$0" | sed 's/^# \{0,1\}//'; exit 0 ;;
    -*) echo "Opção desconhecida: $a"; exit 2 ;;
    *) FILTERS+=("$a") ;;
  esac
done

# ---- po-uilib.jar (o do Yang vem em tests/tests_tiago_yang) ----
UILIB=""
for c in "${PO_UILIB:-}" "$HERE/po-uilib.jar" "$ROOT/po-uilib.jar" "$ROOT"/PO_UILIB/po-uilib.jar \
         "$ROOT"/PO_UILIB/*/po-uilib.jar /usr/share/java/po-uilib.jar; do
  [ -n "$c" ] && [ -f "$c" ] && { UILIB="$c"; break; }
done
[ -f "$UILIB" ] || { echo "Não encontrei o po-uilib.jar"; exit 2; }

if [ $BUILD -eq 1 ]; then
  echo "A compilar..."
  make -s JAVADIR="$(dirname "$UILIB")" || exit 1
fi

TEST_DIR="$HERE/auto-tests"
EXPECTED_DIR="$TEST_DIR/expected"
MYOUT_DIR="$HERE/myout"
CP="$ROOT/erp-app/erp-app.jar:$ROOT/erp-core/erp-core.jar:$UILIB"
MAIN_CLASS="erp.app.App"
for j in "$ROOT/erp-app/erp-app.jar" "$ROOT/erp-core/erp-core.jar"; do
  [ -f "$j" ] || { echo "Falta $j"; exit 1; }
done

# pasta de trabalho: os testes escrevem .dat e alguns dependem de outros
WORK="$HERE/.test-work"; rm -rf "$WORK"; mkdir -p "$WORK"
mkdir -p "$MYOUT_DIR"; rm -f "$MYOUT_DIR"/* 2>/dev/null
cp "$TEST_DIR"/*.in "$WORK"/ 2>/dev/null
cp "$TEST_DIR"/*.import "$WORK"/ 2>/dev/null

total=0; passed=0; failed=0
echo "=== A correr os testes em tests/tests_tiago_yang/auto-tests ==="

for infile in "$TEST_DIR"/*.in; do
    [ -e "$infile" ] || continue
    test_name=$(basename "$infile" .in)
    if [ ${#FILTERS[@]} -gt 0 ]; then
        m=0; for f in "${FILTERS[@]}"; do [[ "$test_name" == *"$f"* ]] && m=1; done
        [ $m -eq 0 ] && continue
    fi
    expected_out="$EXPECTED_DIR/${test_name}.out"
    raw_out="$MYOUT_DIR/${test_name}.myout"
    diff_out="$MYOUT_DIR/${test_name}.diff"
    total=$((total+1))

    if [ -f "$WORK/${test_name}.import" ]; then
        ( cd "$WORK" && timeout 10 java -Dimport="${test_name}.import" -Din="${test_name}.in" -Dout="$raw_out" -cp "$CP" "$MAIN_CLASS" ) >/dev/null 2>&1
    else
        ( cd "$WORK" && timeout 10 java -Din="${test_name}.in" -Dout="$raw_out" -cp "$CP" "$MAIN_CLASS" ) >/dev/null 2>&1
    fi

    if [ -f "$raw_out" ] && diff -u -b -B "$expected_out" "$raw_out" > "$diff_out"; then
        echo -e "[\e[32mPASS\e[0m] $test_name"
        rm -f "$raw_out" "$diff_out"
        passed=$((passed+1))
    else
        echo -e "[\e[31mFAIL\e[0m] $test_name -> diferenças em tests/tests_tiago_yang/myout/${test_name}.diff"
        [ -f "$raw_out" ] || echo "no output" > "$diff_out"
        failed=$((failed+1))
    fi
done

rm -rf "$WORK"
[ "$failed" -eq 0 ] && rmdir "$MYOUT_DIR" 2>/dev/null

echo ""
echo "=== Resumo ==="
echo "Total: $total | Passaram: $passed | Falharam: $failed"
[ "$failed" -eq 0 ]
