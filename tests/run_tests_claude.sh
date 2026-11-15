#!/usr/bin/env bash
# run_tests_claude.sh — corre os testes automáticos de tests/tests_claude/auto-tests do projeto ERP,
# organizados por menu (estilo pytest): Menu Principal, Empresas, Produtos, Clientes, Compras+Cesto.
# Vive em <projeto>/tests/ e corre-se de qualquer sítio (a raiz é a pasta acima, com erp-core, erp-app, Makefile):
#   ./tests/run_tests_claude.sh
#
# Uso:  ./tests/run_tests_claude.sh [opções] [filtro ...]
#   filtro        substring do nome do teste (ex.: A-02  ou  A-13-05)
#   -g GRUPO      só um menu: main | empresas | produtos | clientes | compras  (podes repetir -g)
#   (omissão)     lista PASSED/FAILED por menu e, no fim, uma secção FAILURES com esperado vs obtido
#   -f            diff completo (sem truncar) + input/esperado/obtido dos que falham
#   -v            detalhe completo de todos os testes, também dos que passam
#   -q            só o resumo
#   -s            comparação estrita (distingue maiúsculas/minúsculas; ignora só espaços)
#   -n            não compilar (usa os .jar já existentes)
#   -x            parar no primeiro teste que falhar
#   -t DIR        pasta dos testes (omissão: tests/tests_claude/auto-tests)
#   -j JAR        caminho do po-uilib.jar (omissão: procura automaticamente)
#   -h            ajuda
#
# Os testes são lidos do mesmo sítio de sempre. Cada teste é atribuído ao menu da 1ª opção
# 5/6/7/8 que aparece no .in (5=empresas, 6=produtos, 7=clientes, 8=compras/cesto);
# sem nenhuma dessas, vai para o Menu Principal.
# Por omissão a comparação é igual à da avaliação oficial: ignora espaços,
# mudanças de linha e maiúsculas/minúsculas (diff -iwub -B).

set -u
CALLER_PWD="$PWD"
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
HERE="$ROOT/tests/tests_claude"
cd "$ROOT" || exit 1

VERBOSE=0; FULL=0; QUIET=0; STRICT=0; BUILD=1; STOP=0
TESTS_DIR="$HERE/auto-tests"; UILIB=""; FILTERS=(); GROUPS_SEL=""

abspath() { case "$1" in /*) echo "$1" ;; *) echo "$CALLER_PWD/$1" ;; esac; }

while [ $# -gt 0 ]; do
  case "$1" in
    -v) VERBOSE=1 ;; -f) FULL=1 ;; -q) QUIET=1 ;; -s) STRICT=1 ;; -n) BUILD=0 ;; -x) STOP=1 ;;
    -g) GROUPS_SEL="$GROUPS_SEL $(echo "$2" | tr 'A-Z' 'a-z')"; shift ;;
    -t) TESTS_DIR="$(abspath "$2")"; shift ;;
    -j) UILIB="$(abspath "$2")"; shift ;;
    -h|--help) sed -n '2,22p' "$0" | sed 's/^# \{0,1\}//'; exit 0 ;;
    -*) echo "Opção desconhecida: $1 (usa -h)"; exit 2 ;;
    *) FILTERS+=("$1") ;;
  esac
  shift
done

# ---- cores (só se for terminal) ----
if [ -t 1 ]; then
  R=$'\e[31m'; G=$'\e[32m'; Y=$'\e[33m'; B=$'\e[34m'; D=$'\e[2m'; N=$'\e[0m'; BD=$'\e[1m'
else R=;G=;Y=;B=;D=;N=;BD=; fi

# timeout (macOS não tem por omissão)
if command -v timeout >/dev/null 2>&1; then TO=timeout
elif command -v gtimeout >/dev/null 2>&1; then TO=gtimeout
else TO=""; fi

# ---- localizar po-uilib.jar ----
if [ -z "$UILIB" ]; then
  for c in "${PO_UILIB:-}" /usr/share/java/po-uilib.jar "$ROOT"/PO_UILIB/po-uilib.jar \
           "$ROOT"/PO_UILIB/*/po-uilib.jar "$ROOT"/po-uilib.jar \
           "$ROOT"/tests/tests_tiago_yang/po-uilib.jar; do
    [ -n "$c" ] && [ -f "$c" ] && { UILIB="$c"; break; }
  done
  [ -z "$UILIB" ] && UILIB="$(find "$ROOT" /usr/share/java /opt -iname 'po-uilib*.jar' 2>/dev/null | head -1)"
fi
[ -f "$UILIB" ] || { echo "${R}Não encontrei o po-uilib.jar. Usa:  ./tests/run_tests_claude.sh -j /caminho/po-uilib.jar${N}"; exit 2; }

[ -d "$TESTS_DIR" ] || { echo "${R}Pasta de testes não encontrada: $TESTS_DIR (usa -t DIR)${N}"; exit 2; }
EXP_DIR="$TESTS_DIR/expected"

# ---- compilar ----
if [ $BUILD -eq 1 ]; then
  echo "${BD}== A compilar (make) ==${N}"
  if ! make JAVADIR="$(dirname "$UILIB")" > "$HERE/build.log" 2>&1; then
    echo "${R}Compilação falhou. Últimas linhas (tests/tests_claude/build.log tem tudo):${N}"; tail -40 "$HERE/build.log"; exit 1
  fi
  echo "${G}Compilação OK${N}"
fi
CORE="$ROOT/erp-core/erp-core.jar"; APP="$ROOT/erp-app/erp-app.jar"
for j in "$CORE" "$APP"; do [ -f "$j" ] || { echo "${R}Falta $j${N}"; exit 1; }; done
CP="$UILIB:$CORE:$APP"

WORK="$HERE/.test-work"; rm -rf "$WORK"; mkdir -p "$WORK"
RESULTS="$HERE/test-results"; rm -rf "$RESULTS"; mkdir -p "$RESULTS"
cp "$TESTS_DIR"/*.in "$WORK"/ 2>/dev/null
cp "$TESTS_DIR"/*.import "$WORK"/ 2>/dev/null

norm() { if [ $STRICT -eq 1 ]; then tr '\n\t' '  ' < "$1" | sed -e 's/  */ /g' -e 's/^ //' -e 's/ $//'
         else tr '\n\t' '  ' < "$1" | sed -e 's/  */ /g' -e 's/^ //' -e 's/ $//' | tr 'A-Z' 'a-z'; fi; }

show_block() { echo "${B}--- $1 ---${N}"; sed 's/^/    /' "$2"; [ -n "$(tail -c1 "$2")" ] && echo; echo; }

# ---- grupos (menus) ----
group_name() { case "$1" in
  1) echo "Menu Principal" ;; 2) echo "Empresas" ;; 3) echo "Produtos" ;;
  4) echo "Clientes" ;; 5) echo "Compras e Cesto" ;; esac; }
group_key()  { case "$1" in 1) echo main ;; 2) echo empresas ;; 3) echo produtos ;; 4) echo clientes ;; 5) echo compras ;; esac; }

# devolve o nº do grupo com base na 1ª opção 5/6/7/8 do .in (salta o argumento da opção 4 = avançar data)
classify() {
  local skip=0 l
  while IFS= read -r l || [ -n "$l" ]; do
    l="${l//$'\r'/}"
    if [ $skip -eq 1 ]; then skip=0; continue; fi
    case "$l" in
      4) skip=1 ;;
      5) echo 2; return ;;
      6) echo 3; return ;;
      7) echo 4; return ;;
      8) echo 5; return ;;
    esac
  done < "$1"
  echo 1
}

# 1ª divergência palavra a palavra. 1ª linha = assinatura (para agrupar), restantes = ecrã
first_diff() {
  norm "$1" | tr ' ' '\n' > "$WORK/.a"; norm "$2" | tr ' ' '\n' > "$WORK/.b"
  awk -v R="$R" -v G="$G" -v D="$D" -v N="$N" '
    NR==FNR { a[FNR]=$0; na=FNR; next }
    { b[FNR]=$0; nb=FNR }
    END {
      n=(na>nb?na:nb); d=1
      for(i=1;i<=n;i++) if (a[i]!=b[i]) { d=i; break }
      s=d-6; if (s<1) s=1
      ctx=""; for(i=s;i<d;i++) ctx=ctx a[i] " "
      ea=""; for(i=d;i<d+12 && i<=na;i++) ea=ea a[i] " "
      ob=""; for(i=d;i<d+12 && i<=nb;i++) ob=ob b[i] " "
      if (ea=="") ea="(fim do output esperado)"
      if (ob=="") ob="(output terminou aqui)"
      sig=""; for(i=d;i<d+6 && i<=nb;i++) sig=sig b[i] " "
      if (sig=="") sig="(output terminou aqui)"
      print sig
      printf "  %sesperado:%s %s…%s%s%s\n", D, N, ctx, R, ea, N
      printf "  %sobtido:  %s %s…%s%s%s\n",  D, N, ctx, G, ob, N
    }' "$WORK/.a" "$WORK/.b"
}

colordiff() { # esperado, obtido, [max linhas]
  local fl="-i"; [ $STRICT -eq 1 ] && fl=""
  diff -u $fl --label esperado --label obtido -B -w "$1" "$2" | tail -n +3 | sed \
    -e "s/^-/${R}-/" -e "s/^+/${G}+/" -e "s/^@@/${B}@@/" -e "s/\$/${N}/" | { if [ -n "${3:-}" ]; then head -n "$3"; else cat; fi; } | sed 's/^/    /'
}

full_details() {
  echo "${D}  ------------------------------------------------------------${N}"
  [ -f "$WORK/$name.import" ] && show_block "IMPORT ($name.import)" "$WORK/$name.import"
  show_block "INPUT ($name.in)" "$inf"
  [ -f "$exp" ] && show_block "ESPERADO" "$exp"
  if [ -f "$hyp" ]; then show_block "OBTIDO" "$hyp"
    if [ "$status" = "FAIL" ]; then
      echo "${B}--- DIFF (- esperado / + obtido) ---${N}"; colordiff "$exp" "$hyp"; echo
    fi
  fi
  if [ -s "$err" ]; then echo "${Y}--- STDERR / excepções Java ---${N}"; head -25 "$err" | sed 's/^/    /'; echo; fi
  echo "${D}  ------------------------------------------------------------${N}"
}

# bloco de falha estilo pytest (vai para a secção FAILURES)
failure_block() {
  echo "${R}${BD}_______ [$gname] $name _______${N}"
  echo "${D}input:${N} $(tr '\n' ' ' < "$inf" | cut -c1-140)"
  if [ "$status" = "NOOUT" ]; then
    echo "${R}E   o programa não produziu output (exit=$rc)${N}"
  elif [ "$status" = "NOEXP" ]; then
    echo "${Y}E   falta o ficheiro esperado: $exp${N}"
  else
    if [ $FULL -eq 1 ]; then full_details
    else
      echo "${R}E   assert obtido == esperado${N}"
      out="$(first_diff "$exp" "$hyp")"
      printf '%s\t%s\n' "$(echo "$out" | head -1)" "$name" >> "$WORK/.sigs"
      echo "$out" | tail -n +2
      echo "${D}  diff (- esperado / + obtido), ver -f para completo:${N}"
      colordiff "$exp" "$hyp" 30
    fi
  fi
  if [ $FULL -eq 0 ] && [ -s "$err" ]; then echo "${Y}  stderr:${N} $(head -3 "$err" | tr '\n' ' ' | cut -c1-160)"; fi
  echo
}

# ---- construir lista (grupo|nome) ----
LIST="$WORK/.list"; : > "$LIST"
shopt -s nullglob
for inf in "$TESTS_DIR"/*.in; do
  name="$(basename "$inf" .in)"
  if [ ${#FILTERS[@]} -gt 0 ]; then
    m=0; for f in "${FILTERS[@]}"; do [[ "$name" == *"$f"* ]] && m=1; done
    [ $m -eq 0 ] && continue
  fi
  g="$(classify "$inf")"
  if [ -n "$GROUPS_SEL" ]; then
    case " $GROUPS_SEL " in *" $(group_key "$g") "*) ;; *) continue ;; esac
  fi
  echo "$g|$name" >> "$LIST"
done
sort -t'|' -k1,1n -k2,2 "$LIST" -o "$LIST"

PASS=0; FAIL=0; NOOUT=0; TOTAL=0; FAILED_LIST=(); LASTG=""
FAILS="$WORK/.failures"; : > "$FAILS"; RES="$WORK/.res"; : > "$RES"
START=$SECONDS

while IFS='|' read -r g name; do
  inf="$TESTS_DIR/$name.in"
  gname="$(group_name "$g")"
  if [ "$g" != "$LASTG" ]; then
    LASTG="$g"
    [ $QUIET -eq 0 ] && { echo; echo "${BD}${B}━━ $gname ━━${N}"; }
  fi
  TOTAL=$((TOTAL+1))
  exp="$EXP_DIR/$name.out"; hyp="$WORK/$name.outhyp"; err="$RESULTS/$name.stderr"
  rm -f "$hyp"

  props=(-Din="$name.in" -Dout="$name.outhyp")
  [ -f "$WORK/$name.import" ] && props=(-Dimport="$name.import" "${props[@]}")
  ( cd "$WORK" && $TO ${TO:+10} java -cp "$CP" "${props[@]}" erp.app.App ) > /dev/null 2> "$err"
  rc=$?

  status=""
  if [ ! -f "$hyp" ]; then status="NOOUT"
  elif [ ! -f "$exp" ]; then status="NOEXP"
  elif [ "$(norm "$exp")" == "$(norm "$hyp")" ]; then status="PASS"
  else status="FAIL"; fi

  case "$status" in
    PASS) PASS=$((PASS+1)) ;;
    FAIL) FAIL=$((FAIL+1)); FAILED_LIST+=("[$gname] $name") ;;
    NOOUT) NOOUT=$((NOOUT+1)); FAILED_LIST+=("[$gname] $name") ;;
    NOEXP) FAIL=$((FAIL+1)); FAILED_LIST+=("[$gname] $name") ;;
  esac
  echo "$g|$status" >> "$RES"

  if [ $QUIET -eq 0 ]; then
    case "$status" in
      PASS)  printf "  %s✔ PASSED%s  %s\n" "$G" "$N" "$name" ;;
      FAIL)  printf "  %s✘ FAILED%s  %s\n" "$R" "$N" "$name" ;;
      NOOUT) printf "  %s✘ NO OUTPUT%s  %s\n" "$R" "$N" "$name" ;;
      NOEXP) printf "  %s? NO EXPECTED%s  %s\n" "$Y" "$N" "$name" ;;
    esac
  fi

  # relatórios em ficheiro (sempre)
  [ -f "$hyp" ] && cp "$hyp" "$RESULTS/$name.obtained"
  [ -f "$exp" ] && [ -f "$hyp" ] && diff -u "$exp" "$hyp" > "$RESULTS/$name.diff" 2>/dev/null
  [ -s "$err" ] || rm -f "$err"

  if [ $VERBOSE -eq 1 ] && [ $QUIET -eq 0 ]; then full_details
  elif [ "$status" != "PASS" ]; then failure_block >> "$FAILS"; fi

  [ $STOP -eq 1 ] && [ "$status" != "PASS" ] && break
done < "$LIST"

# ---- secção FAILURES (como o pytest) ----
if [ $QUIET -eq 0 ] && [ $VERBOSE -eq 0 ] && [ -s "$FAILS" ]; then
  echo; echo "${BD}${R}======================= FAILURES =======================${N}"; echo
  cat "$FAILS"
fi

if [ $QUIET -eq 0 ] && [ -s "$WORK/.sigs" ] && [ "$FAIL" -gt 1 ]; then
  echo "${BD}Divergências mais comuns (o que o programa escreveu em vez do esperado):${N}"
  cut -f1 "$WORK/.sigs" | sort | uniq -c | sort -rn | head -8 | while read -r c sig; do
    printf "  %3s×  %s\n" "$c" "$sig"
  done
  echo
fi

# ---- resumo por menu ----
echo "${BD}================ RESUMO POR MENU ================${N}"
for g in 1 2 3 4 5; do
  t=$(grep -c "^$g|" "$RES"); [ "$t" -eq 0 ] && continue
  p=$(grep -c "^$g|PASS$" "$RES")
  if [ "$p" -eq "$t" ]; then c="$G"; else c="$R"; fi
  printf "  %-18s %s%s/%s%s\n" "$(group_name $g)" "$c" "$p" "$t" "$N"
done
echo
if [ ${#FAILED_LIST[@]} -gt 0 ]; then
  echo "${R}Falhados:${N}"; for f in "${FAILED_LIST[@]}"; do echo "  - $f"; done
  echo "${D}Detalhe completo: ./tests/run_tests_claude.sh -f <teste>   (ou -v)${N}"
  echo "${D}Diffs e outputs guardados em: tests/tests_claude/test-results/<teste>.diff|.obtained|.stderr${N}"
  echo
fi
SECS=$((SECONDS-START))
if [ $((FAIL+NOOUT)) -gt 0 ]; then
  echo "${R}${BD}=== $((FAIL+NOOUT)) failed, $PASS passed em ${SECS}s ===${N}"
else
  echo "${G}${BD}=== $PASS passed em ${SECS}s ===${N}"
fi
[ $TOTAL -gt 0 ] && printf "Nota (%% de testes): %s%%\n" "$(( PASS * 100 / TOTAL ))"
rm -rf "$WORK"
[ $((FAIL+NOOUT)) -eq 0 ]
