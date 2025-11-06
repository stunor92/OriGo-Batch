# Security Policy

## Supported Versions

We release patches for security vulnerabilities. The following versions are currently supported with security updates:

| Version | Supported          |
| ------- | ------------------ |
| 3.0.x   | :white_check_mark: |
| 2.2.x   | :white_check_mark: |
| < 2.2   | :x:                |

## Reporting a Vulnerability

We take the security of OriGo-Batch seriously. If you have discovered a security vulnerability, we appreciate your help in disclosing it to us in a responsible manner.

### How to Report a Security Vulnerability

**Please do not report security vulnerabilities through public GitHub issues.**

Instead, please report them via one of the following methods:

1. **GitHub Security Advisories** (preferred): Use the [GitHub Security Advisory](https://github.com/stunor92/OriGo-Batch/security/advisories/new) feature to privately report vulnerabilities.

2. **Email**: Send an email to the repository maintainer through GitHub. You can find contact information on the [GitHub profile](https://github.com/stunor92).

### What to Include

Please include the following information in your report:

- Type of vulnerability (e.g., SQL injection, cross-site scripting, etc.)
- Full paths of source file(s) related to the manifestation of the vulnerability
- The location of the affected source code (tag/branch/commit or direct URL)
- Any special configuration required to reproduce the issue
- Step-by-step instructions to reproduce the issue
- Proof-of-concept or exploit code (if possible)
- Impact of the issue, including how an attacker might exploit it

### Response Timeline

- We will acknowledge receipt of your vulnerability report within 48 hours
- We will provide a more detailed response within 5 business days indicating the next steps
- We will keep you informed about the progress towards a fix and announcement
- We may ask for additional information or guidance

### Safe Harbor

We support safe harbor for security researchers who:

- Make a good faith effort to avoid privacy violations, destruction of data, and interruption or degradation of our services
- Only interact with accounts you own or with explicit permission of the account holder
- Do not exploit a security issue for any reason (this includes demonstrating additional risk, such as attempting to access sensitive data)

## Security Best Practices

When deploying OriGo-Batch, please ensure:

1. **Environment Variables**: Never commit sensitive information like database passwords or API keys to version control. Always use environment variables or secure secret management systems.

2. **Database Security**: 
   - Use strong passwords for database connections
   - Ensure PostgreSQL is properly configured with appropriate access controls
   - Use encrypted connections to the database when possible

3. **API Keys**: Protect Eventor API keys and rotate them regularly. Store them securely using environment variables or secret management solutions.

4. **Container Security**: 
   - Keep the base Docker images up to date
   - Scan container images for vulnerabilities regularly
   - Follow the principle of least privilege when running containers

5. **Dependencies**: Keep all dependencies up to date. This project uses Dependabot to automatically check for dependency updates.

6. **Network Security**: Run the application in a secure network environment with appropriate firewall rules and access controls.

## Security Updates

Security updates will be released as patch versions and documented in the [CHANGELOG.md](CHANGELOG.md). Critical security issues will be highlighted in release notes.

## Acknowledgments

We appreciate the security research community's efforts in helping keep OriGo-Batch and its users safe.
