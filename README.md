# OrleansCommands

OrleansCommands is a Minecraft Paper plugin written in Java, designed to provide a suite of quality-of-life commands and
features for VIP players on the OrleansMC server. The plugin includes custom commands for inventory access, item repair,
player head retrieval, skin changes, and more, with integrated cooldowns and permission checks.

---

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technologies](#technologies)
- [Installation](#installation)
- [Usage](#usage)
- [Configuration](#configuration)
- [Development](#development)
- [Limitations](#limitations)
- [License](#license)
- [Credits](#credits)

---

## Overview

OrleansCommands enhances the player experience by offering a set of VIP commands, each with configurable cooldowns and
permission requirements. The plugin is intended for use on the OrleansMC server but can be adapted for other Paper-based
Minecraft servers.

---

## Features

- **VIP Commands:**
   - `/crafting` – Open a crafting table anywhere
   - `/enderchest` – Access your ender chest
   - `/enchanting` – Open an enchanting table
   - `/anvil` – Open an anvil interface
   - `/repair` – Instantly repair the item in your hand (with cooldown)
   - `/feed` – Restore your hunger (with cooldown)
   - `/head <player>` – Get a player’s head (with cooldown)
   - `/skin <player>` – Change your skin to another player’s (with cooldown)
- **Promotion System:**
   - `/promotion <code>` – Use a referral code for rewards
- **Cooldown Management:**
   - Per-group cooldowns, stored in Redis and configured via `config.yml`
- **Permission Integration:**
   - LuckPerms support for group-based command access and cooldowns
- **Discord Webhook Integration:**
   - Sends notifications for certain actions (e.g., promotions)

---

## Technologies

- **Java 21**
- **Paper API 1.21.1**
- **Gradle**
- **LuckPerms API**
- **PlaceholderAPI**
- **Redis (via RedisEconomy)**
- **SkinsRestorer API**
- **Helper (Lucko’s library)**

---

## Installation

1. **Requirements:**
   - Paper 1.21.1 server
   - Java 21 or newer
   - Redis server (for cooldowns)
   - [LuckPerms](https://luckperms.net/)
   - [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/)
   - [SkinsRestorer](https://www.spigotmc.org/resources/skinsrestorer.2124/)
   - [Helper](https://github.com/lucko/helper)
   - [RedisEconomy](https://github.com/Emibergo02/RedisEconomy)
   - **OrleansMC** core plugin (see below)

2. **Build:**
   - Clone this repository.
   - Run `./gradlew shadowJar` to build the plugin.
   - The JAR will be in `build/libs`.

3. **Install:**
   - Place the JAR in your server’s `plugins` folder.
   - Ensure all dependencies are installed.
   - **Important:** Download or build the [OrleansMC](https://github.com/OrleansMC/OrleansMC) core plugin.  
     Place the resulting `OrleansMC-0.1-all.jar` file into the `lib` folder.

4. **Start:**
   - Start or restart your server.

---

## Usage

- Use `/help` or `/[command]` in-game to see available commands and their usage.
- Only players with the correct LuckPerms permissions can use VIP commands.
- Cooldowns are enforced per player and per group, as configured.

---

## Configuration

The main configuration file is `config.yml`.  
Example cooldown section:

```yaml
cooldowns:
  lord:
    repair: -1
    feed: 3600
    head: 1200
    skin: 10800
  titan:
    repair: -1
    feed: 1800
    head: 600
    skin: 10800
  # ... more groups
```

- Set cooldowns (in seconds) for each group and command.
- `-1` disables the cooldown for that group/command.

---

## Development

- The codebase uses Java records and modern APIs.
- Commands are registered via Lucko’s Helper library.
- Redis is used for distributed cooldown tracking.
- Contributions are welcome, but the code is tailored for OrleansMC and may require adaptation for other servers.

---

## Limitations

- Some features are tightly coupled to OrleansMC infrastructure.
- Not all commands may be relevant for every server.
- Documentation is minimal; review the source code for details.

---

## License

This project is licensed under the [MIT License](https://opensource.org/license/mit/).
See the [`LICENSE`](./LICENSE.txt) file for details.

---

## Credits

- [Lucko’s Helper](https://github.com/lucko/helper)
- [LuckPerms](https://luckperms.net/)
- [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/)
- [SkinsRestorer](https://www.spigotmc.org/resources/skinsrestorer.2124/)
- [RedisEconomy](https://github.com/Emibergo02/RedisEconomy)
- The OrleansMC community

---

For questions or issues, please use GitHub Issues or Pull Requests.