# Decision 002: Add Unit Test Suite

## Status
Accepted

## Context
MyTrip had no automated tests. All validation was manual — start a test server, install the plugin, and interact in-game. This made it difficult to catch regressions, verify behavior after dependency updates, and onboard contributors.

The Spigot 1.21 + CrucialLib 3.0 update (Decision 001) increased the need for automated validation since multiple API changes were made simultaneously.

## Decision
- Add JUnit 5 (`junit-jupiter:5.11.0`) as the test framework
- Add MockBukkit (`mockbukkit-v1.21:4.101.0`) for Bukkit API simulation in tests
- Add `maven-surefire-plugin:3.2.5` for Maven test execution
- Create a CI pipeline (`.github/workflows/ci.yml`) using GitHub Actions
- Write unit tests covering:
  - Utility classes (`MathUtils`, `ConfigStrings`)
  - API registration system (`APICaller`)
  - All test dependencies scoped to `test`

## Test Coverage Strategy

### What is tested
- **MathUtils**: Random number generation within bounds, edge cases
- **ConfigStrings**: Constant values, permission string formats, UUID validity
- **APICaller**: Event registration/unregistration for all event types, error handling for unsupported events

### What is not yet tested (and why)
- **Plugin loading** (`MockBukkit.load(MyTrip.class)`): Requires CrucialLib to be present as a plugin in the test server, which MockBukkit doesn't support loading. The `Crucial.init()` call in `onLoad()` would disable the plugin.
- **Commands**: Depend on plugin being loaded and CrucialLib localization being initialized
- **Drug consumption/effects**: Depend on loaded drugs from JSON, CrucialItem registration, and BukkitScheduler
- **GUI inventories**: Depend on CrucialLib's Page system being initialized
- **File I/O**: Depends on CrucialLib's Json utility and external file downloads

### Future improvements
- Add integration tests that mock the CrucialLib plugin
- Test command routing logic by extracting it from CommandListener
- Test DrugPlayer dose/addiction logic by decoupling from static plugin reference

## CI Configuration
- Triggers on push/PR to `master` and `main` branches
- Uses `ubuntu-latest` with Temurin Java 21
- Runs `mvn -B verify` (compile + test + package)
- Generates test report via `dorny/test-reporter`

## Consequences

### Positive
- Automated regression detection for utility and API code
- CI enforces that all tests pass before merge
- Foundation for expanding test coverage as CrucialLib mocking support improves
- New contributors have a clear pattern for adding tests

### Negative
- MockBukkit dependency adds complexity to the test setup
- Cannot fully test plugin lifecycle without CrucialLib mock support
- Test coverage is limited to decoupled logic for now

### Requirements going forward
- All new features must include tests where feasible
- CI must pass before merging pull requests
- As code is refactored, move toward dependency injection patterns that enable more thorough testing
