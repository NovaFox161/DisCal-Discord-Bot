# DisCal Discord Bot

[![Discord](https://img.shields.io/discord/375357265198317579?label=DreamExposure&style=flat-square)](https://discord.gg/2TFqyuy)
[![Jenkins](https://img.shields.io/jenkins/build?jobUrl=https%3A%2F%2Fjenkins.dreamexposure.org%2Fview%2FDiscord-Bots%2Fjob%2FDisCal%2F&style=flat-square)](https://jenkins.dreamexposure.org/job/DisCal/)
[![Jenkins tests](https://img.shields.io/jenkins/tests?compact_message&jobUrl=https%3A%2F%2Fjenkins.dreamexposure.org%2Fview%2FDiscord-Bots%2Fjob%2FDisCal%2F&style=flat-square)](https://jenkins.dreamexposure.org/job/DisCal/tests)

DisCal is a discord bot that connects Discord and Google Calendar as seamlessly as possible with a wide feature set for calendar management and information.

# Core Features

* Powerful in-server integration of google calendar
* Custom Calendar creation and editing
* Event creation, editing, and deletion
* Automated announcement system to remind users of events
* Customizable prefix and mentionable commands
* Versatile and built for all communities
* Web dashboard for bot and calendar management

## Main Patron-Only Features

Patrons and supporters on the $5/month plans get access to work in progress and exclusive features.

* External Calendars
    - Use an already existing calendar that is on your Google account with DisCal
* Web Dashboard (WIP)
    - Use the web dashboard to manage the bot, calendar, and more without the need for commands.
    - Still very early in development
* Server Branding
    - Hide the DisCal name in favor of using your server's name on announcements and embeds.
* Announcement Publishing
    - Announcements posted in news channels can be (optionally) automatically "published" so servers following the news
      channel receive them as well!

### Smaller Patron-only Features

* Gif support for event images
* Automatically assign roles to users when RSVPing to an event.

## Planned Features & Work in Progress Changes

* Multiple calendars per server (WIP)
* Advanced announcement configuration (WIP)
* Complex recurring event configuration (WIP)
* Proper patreon integration for automated setup.
* Better translation support (Right now using the JSON files is really messy and hard to maintain)
* And so much more!

# Quick Links

* [Website](https://www.discalbot.com)
* [Discord](https://discord.gg/2TFqyuy)
* [Trello](https://trello.com/b/2Xx3Galz)

# Technologies

DisCal is primarily written in Java with a TypeScript powered web-frontend.
We use the following technologies throughout the project:
* [Discord4J](https://github.com/Discord4J/Discord4J) API wrapper
* Project Reactor for fully reactive code
* SpringBoot web backend
* MySQL with Redis caching

# Developer RESTful API

DisCal was written for the community, and to aid in that goal, DisCal has a fully functioning REST API to allow developers to bring their applications to DisCal.

Current API Version: v2

To get an API token, please contact the development team.
* [API Docs](https://www.discalbot.com/docs/api/overview)

# Contributing

DisCal is an open source project and is maintained in our free time. We always welcome and love contributions.

## Code

1. Fork this repo and make changes in your own copy
2. Write your code and add any new tests if applicable
3. Run the new and/or existing tests with `mvn clean test` to make sure they pass
4. Commit your changes and push to your fork `git push origin master`
5. Create a new pull request and submit it back to us!

> This section will be getting a facelift

## Translations

As a calendar bot, DisCal reaches global use, and because of this , Discal allows users to select the language they wish
to use.

> This section is a work in progress as it needs to be rewritten for our new translation system
