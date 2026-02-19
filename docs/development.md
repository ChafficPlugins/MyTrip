# Development

## Building from Source

### Requirements

- **Java 21** — install via [Adoptium](https://adoptium.net/) or your preferred distribution
- **Maven** — 3.8+ recommended
- **Git**

### Clone and Build

```bash
git clone https://github.com/ChafficPlugins/MyTrip.git
cd MyTrip
mvn clean package
```

The plugin JAR will be at `target/MyTrip-v0.8.0.jar`.

### Running Tests

```bash
mvn test             # Run unit tests only
mvn clean verify     # Full build + test cycle
```

Tests use [JUnit 5](https://junit.org/junit5/) and [MockBukkit](https://github.com/MockBukkit/MockBukkit) for Bukkit API simulation.

## Project Structure

```
src/main/java/de/chafficplugins/mytrip/
├── MyTrip.java              # Plugin main class
├── api/                     # Developer API (event hooks)
├── drugs/
│   ├── commands/            # /mytrip command handler
│   ├── events/              # Bukkit event listeners
│   ├── inventories/         # GUI pages (CrucialLib Page)
│   └── objects/             # Data models (MyDrug, DrugPlayer, Addiction, DrugTool)
├── io/                      # JSON file I/O, messages.yml
└── utils/                   # Constants, CrucialLib connection, localization, utilities
```

See [ARCHITECTURE.md](https://github.com/ChafficPlugins/MyTrip/blob/master/ARCHITECTURE.md) for detailed documentation of each package and class.

## Dependencies

| Dependency | Version | Scope | Purpose |
|---|---|---|---|
| [Spigot API](https://hub.spigotmc.org/) | 1.21.4-R0.1-SNAPSHOT | provided | Minecraft server API |
| [CrucialLib](https://github.com/ChafficPlugins/CrucialLib) | v3.0.0 | provided | Custom items, GUI, localization, JSON I/O |
| [JetBrains Annotations](https://github.com/JetBrains/java-annotations) | 19.0.0 | compile | Nullability annotations |
| [JUnit 5](https://junit.org/junit5/) | 5.11.0 | test | Unit testing framework |
| [MockBukkit](https://github.com/MockBukkit/MockBukkit) | 4.101.0 | test | Bukkit API mocking |

## CI/CD

CI and docs deployment are handled by GitHub Actions workflows in `.github/workflows/`:

- **`ci.yml`** — Runs `mvn -B verify` on every push and PR to `master`/`main`. Generates a test report and uploads the built JAR as an artifact. All tests must pass before merging.
- **`trigger-docs.yml`** — Triggers a rebuild of the [docs website](https://chafficplugins.github.io) whenever files in `docs/` change on `master`.

## Contributing

### Branch Naming

- `feature/<description>` — new features
- `fix/<description>` — bug fixes
- `chore/<description>` — maintenance, dependency updates, docs

### Testing Requirements

- New features should include unit tests where feasible
- Use MockBukkit for tests involving Bukkit API interactions
- All tests must pass: `mvn test`

### PR Checklist

- [ ] All tests pass (`mvn test`)
- [ ] Code compiles without errors
- [ ] No new compiler warnings introduced
- [ ] User-facing changes documented
- [ ] No breaking config changes without migration path
- [ ] New commands include permission checks

### Manual Testing

1. Set up a Spigot/Paper 1.21+ test server
2. Install CrucialLib v3.0.0 in `plugins/`
3. Build MyTrip (`mvn clean package`) and copy the JAR to `plugins/`
4. Start the server and verify:
   - Plugin loads without errors
   - `/mt help` shows command list
   - `/mt list` opens the drug GUI
   - `/mt create testdrug` opens the creation workflow
   - Drug consumption applies effects correctly
   - Drug Set crafting works when enabled

See [CONTRIBUTING.md](https://github.com/ChafficPlugins/MyTrip/blob/master/CONTRIBUTING.md) for the full contribution guide.
