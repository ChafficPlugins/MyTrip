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

The plugin JAR will be at `target/MyTrip-v0.7.6.jar`.

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

The project uses GitHub Actions for continuous integration. The workflow (`.github/workflows/ci.yml`) runs on every push and pull request to `master`/`main`:

1. Sets up Java 21 (Temurin)
2. Runs `mvn -B verify` (compile, test, package)
3. Generates a test report
4. Uploads the built JAR as a workflow artifact

All tests must pass before merging.

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
