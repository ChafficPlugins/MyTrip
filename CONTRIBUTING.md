# Contributing to MyTrip

## Development Environment Setup

1. **Java 21** — install via [Adoptium](https://adoptium.net/) or your preferred distribution
2. **Maven** — 3.8+ recommended
3. Import as a Maven project in your IDE (IntelliJ IDEA, Eclipse, etc.)
4. Dependencies resolve automatically from Maven Central, JitPack, and SpigotMC repositories

## Building

```bash
mvn clean package    # Build plugin JAR
mvn test             # Run unit tests only
mvn clean verify     # Full build + test
```

## Branch Naming

- `feature/<description>` — new features
- `fix/<description>` — bug fixes
- `chore/<description>` — maintenance, dependency updates, docs

## Testing Requirements

- New features must include unit tests
- Use **MockBukkit** for any tests involving Bukkit API interactions (players, inventories, events)
- Use **JUnit 5** for all tests
- Tests live in `src/test/java/` mirroring the main package structure
- All tests must pass before merging: `mvn test`

### Writing Tests

```java
// Pure logic tests
@Test
void myMethod_condition_expectedBehavior() {
    // arrange, act, assert
}
```

## Code Style

- Follow existing conventions in the codebase
- Use Java switch expressions (arrow syntax) where appropriate
- Constants go in `ConfigStrings`
- Permission strings follow the `mytrip.<category>.<action>` pattern
- Static plugin references: `MyTrip.getPlugin(MyTrip.class)`

## PR Checklist

- [ ] All tests pass (`mvn test`)
- [ ] Code compiles without errors (`mvn compile`)
- [ ] No new compiler warnings introduced
- [ ] User-facing changes are documented (wiki update if needed)
- [ ] No breaking config changes without migration path
- [ ] New drug properties include appropriate validation (see `setOverdose()`, `setDuration()`, etc.)
- [ ] New commands include permission checks and are documented in help output
- [ ] New events are registered in `MyTrip.onEnable()`

## Manual Testing

1. Set up a test Spigot/Paper 1.21+ server
2. Install CrucialLib v3.0.0 in the plugins folder
3. Build MyTrip (`mvn clean package`) and copy the JAR from `target/` to the plugins folder
4. Start the server and verify:
   - Plugin loads without errors in console
   - `/mt help` shows command list
   - `/mt list` opens the drug GUI
   - `/mt create testdrug` opens the creation workflow
   - Drug consumption applies effects correctly
   - Drug set crafting works when enabled
5. Check `plugins/MyTrip/` for generated config.yml, messages.yml, and data files

## Project Structure

See [ARCHITECTURE.md](ARCHITECTURE.md) for detailed information about the codebase structure, data flow, and CrucialLib integration.
