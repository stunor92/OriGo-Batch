# Contributing to OriGo-Batch

Thank you for your interest in contributing to OriGo-Batch! We welcome contributions from the community.

## Code of Conduct

Please be respectful and considerate in your interactions with other contributors.

## How to Contribute

1. Fork the repository
2. Create a feature branch from `main`
3. Make your changes
4. Ensure all tests pass
5. Submit a pull request

## Commit Message Convention

This project follows the [Conventional Commits](https://www.conventionalcommits.org/) specification. All commit messages must be formatted accordingly.

### Commit Message Format

Each commit message consists of a **header**, an optional **body**, and an optional **footer**:

```
<type>(<scope>): <subject>

<body>

<footer>
```

#### Type

The type must be one of the following:

- **feat**: A new feature
- **fix**: A bug fix
- **docs**: Documentation only changes
- **style**: Changes that do not affect the meaning of the code (white-space, formatting, missing semi-colons, etc)
- **refactor**: A code change that neither fixes a bug nor adds a feature
- **perf**: A code change that improves performance
- **test**: Adding missing tests or correcting existing tests
- **build**: Changes that affect the build system or external dependencies
- **ci**: Changes to our CI configuration files and scripts
- **chore**: Other changes that don't modify src or test files
- **revert**: Reverts a previous commit

#### Scope

The scope is optional and can be anything specifying the place of the commit change. For example:

- `feat(api)`: A new feature in the API
- `fix(database)`: A bug fix in the database layer
- `docs(readme)`: Documentation update in the README

#### Subject

The subject contains a succinct description of the change:

- Use the imperative, present tense: "change" not "changed" nor "changes"
- Don't capitalize the first letter
- No period (.) at the end

#### Body (Optional)

The body should include the motivation for the change and contrast this with previous behavior.

#### Footer (Optional)

The footer should contain any information about **Breaking Changes** and is also the place to reference GitHub issues that this commit closes.

**Breaking Changes** should start with the word `BREAKING CHANGE:` with a space or two newlines.

### Examples

#### Feature with scope
```
feat(organizations): add support for organization hierarchy

Implement parent-child relationships between organizations
to better represent the structure of orienteering clubs.
```

#### Bug fix
```
fix(batch): prevent duplicate organization entries

Check for existing organizations before inserting new ones
to avoid constraint violations.

Fixes #123
```

#### Breaking change
```
feat(api)!: change organization ID format to UUID

BREAKING CHANGE: Organization IDs are now UUIDs instead of integers.
This requires database migration for existing installations.
```

#### Documentation update
```
docs: update installation instructions for PostgreSQL setup
```

#### Chore
```
chore(deps): update Spring Boot to 3.5.7
```

## Pull Request Guidelines

- Pull request titles must follow the same Conventional Commits format
- Keep pull requests focused on a single feature or bug fix
- Update documentation as needed
- Ensure all CI checks pass before requesting review
- Link related issues in the pull request description

## Development Setup

See the [README.md](README.md) for instructions on setting up your development environment.

## Testing

Run tests with:

```bash
./mvnw test
```

## Questions?

If you have questions about contributing, please open an issue for discussion.
