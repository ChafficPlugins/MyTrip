#!/usr/bin/env bash
set -euo pipefail

COMPOSE_DIR="$(cd "$(dirname "$0")" && pwd)"
PLUGINS_DIR="$COMPOSE_DIR/plugins"
CONTAINER="mytrip-server"

usage() {
  cat <<EOF
Usage: $0 <command> [args]

Commands:
  build         Build MyTrip with Maven and copy the JAR to plugins/
  add <jar>     Copy a plugin JAR into plugins/
  remove <name> Remove a plugin JAR by filename (supports glob)
  list          List currently deployed plugins
  reload        Send "reload confirm" to the running server
  start         Start the server (docker compose up -d)
  stop          Stop the server
  logs          Tail server logs
  console       Attach to the server console (Ctrl-C to detach)
  cmd <command> Send a command to the running server console
  status        Show container status

Examples:
  $0 build                          # Build MyTrip and deploy it
  $0 add ~/Downloads/SomePlugin.jar # Deploy an external plugin
  $0 remove SomePlugin.jar          # Remove a plugin
  $0 cmd "give Player diamond 64"   # Run a server command
  $0 reload                         # Reload plugins
EOF
  exit 1
}

ensure_plugins_dir() {
  mkdir -p "$PLUGINS_DIR"
}

cmd_build() {
  local mvn_args=()
  if [[ "${USE_LOCAL_CRUCIALLIB:-}" == "1" ]]; then
    echo "==> Building MyTrip with local CrucialLib v3.0.1..."
    mvn_args+=("-Dcruciallib.version=v3.0.1")
  else
    echo "==> Building MyTrip..."
  fi
  mvn -f "$COMPOSE_DIR/pom.xml" clean package -q "${mvn_args[@]}"
  ensure_plugins_dir
  cp "$COMPOSE_DIR"/target/MyTrip-*.jar "$PLUGINS_DIR/"
  echo "==> Deployed to plugins/:"
  ls "$PLUGINS_DIR"/MyTrip-*.jar
}

cmd_add() {
  local jar="$1"
  if [[ ! -f "$jar" ]]; then
    echo "Error: file not found: $jar"
    exit 1
  fi
  ensure_plugins_dir
  local src dest
  src="$(realpath "$jar")"
  dest="$PLUGINS_DIR/$(basename "$jar")"
  if [[ "$src" == "$(realpath "$dest" 2>/dev/null)" ]]; then
    echo "==> $(basename "$jar") is already in plugins/"
  else
    cp "$jar" "$PLUGINS_DIR/"
    echo "==> Deployed $(basename "$jar") to plugins/"
  fi
}

cmd_remove() {
  local name="$1"
  local target="$PLUGINS_DIR/$name"
  # shellcheck disable=SC2086
  if ls $target 1>/dev/null 2>&1; then
    rm $target
    echo "==> Removed $name"
  else
    echo "Error: no plugin matching '$name' in plugins/"
    exit 1
  fi
}

cmd_list() {
  ensure_plugins_dir
  echo "==> Plugins in $PLUGINS_DIR:"
  ls -1 "$PLUGINS_DIR"/*.jar 2>/dev/null || echo "  (none)"
}

cmd_reload() {
  docker exec "$CONTAINER" rcon-cli reload confirm
}

cmd_start() {
  ensure_plugins_dir
  docker compose -f "$COMPOSE_DIR/docker-compose.yml" up -d
  echo "==> Server starting. Use '$0 logs' to follow startup."
}

cmd_stop() {
  docker compose -f "$COMPOSE_DIR/docker-compose.yml" down
}

cmd_logs() {
  docker compose -f "$COMPOSE_DIR/docker-compose.yml" logs -f
}

cmd_console() {
  echo "Attaching to server console. Press Ctrl-C to detach."
  docker attach "$CONTAINER"
}

cmd_cmd() {
  docker exec "$CONTAINER" rcon-cli "$*"
}

cmd_status() {
  docker compose -f "$COMPOSE_DIR/docker-compose.yml" ps
}

[[ $# -lt 1 ]] && usage

case "$1" in
  build)   cmd_build ;;
  add)     [[ $# -lt 2 ]] && { echo "Error: specify a JAR path"; exit 1; }; cmd_add "$2" ;;
  remove)  [[ $# -lt 2 ]] && { echo "Error: specify a filename"; exit 1; }; cmd_remove "$2" ;;
  list)    cmd_list ;;
  reload)  cmd_reload ;;
  start)   cmd_start ;;
  stop)    cmd_stop ;;
  logs)    cmd_logs ;;
  console) cmd_console ;;
  cmd)     shift; cmd_cmd "$@" ;;
  status)  cmd_status ;;
  *)       usage ;;
esac
