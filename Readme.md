# Green Team Auto Manager

## History

While finishing up from my NorthWest education, my dad came up with a project idea for somekind of software to help his business as a way to keep me in practice of writing code. I chose to use android studio because he does not own a computer but he does have android devices. For this project I will be learning the Kotlin programming language as well as the android studio framework.

## Description

The Green Team Auto Manager (GTAM) is my software solution for my dad's business. In this project, GTAM is an android application designed to trivialize the process of sending business emails and invoices. Using room database, GTAM stores data regarding clients, email addresses, phone numbers, API key information and messaging history. GTAM utilizes API calls from Mailjet and Numverify in order to send messages.

## Installation

Currently, the app is in closed testing on google play. This means that not just anyone could follow the link and see the app listed in the store. If you are interested in downloading the GTAM app from google play, you will need to provide me an email to add to the testers list. Once your email is added to the list, make sure to use that email when you use the link to google play.

Message me at micahg355be@gmail.com if you want me to add your email to the testing list.
[Play Store Link](https://play.google.com/store/apps/details?id=com.ms.gtam)

## Setup

As mensioned before, GTAM uses both Mailjet and Numverify APIs to send messages, you will need to collect API keys from both websites to operate this app. Dont worry though, you can get a free plan with both websites. The Mailjet API is used to send emails regardless of sender address, the free plan has a limit of 6000~ emails per month. Numverify's API is used if the client uses a phone number rather than an email which the system needs to know the carrier to send messages. The free plan for Numverify is 100 calls per month.

- [Mailjet Website](https://www.mailjet.com/)
- [Numverify Website](https://numverify.com/)
