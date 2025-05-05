package com.example.gtam

object Strings {
    // Activity 1
    const val A1M1 = "Default system text for the subject of the email. The subject will be ignored if the message is a text."
    const val A1M2 = "Default system text for the start of the email, before any listed services."
    const val A1M3 = "Default system text for the end of the email, after any listed services."

    // Activity 1 Placeholders
    const val A1P1 = "The Green Team of Saltillo"

    const val A1P2 = "Hello, this is (your name here)." +
            "\nI just wanted to follow up with a quick summary of the work completed and provide payment details.\n"

    const val A1P3 = "You can send the payment to:\n(preferred method)" +
            "\n\nIf you have any questions or need anything else, feel free to reach out. Thanks again for your business!" +
            "\nBest regards,\n(your name here)" +
            "\n\nThis message was sent automatically. If you have any questions, " +
            "just reach out at (personal email address) — I’ll get back to you soon."

    const val A1P4 = "Hello, this is (your name here). " +
            "I just wanted to follow up with a quick summary of the work completed and provide payment details.\n"

    const val A1P5 = "You can send the payment to: (preferred method)" +
    "\nThis message was sent automatically - replies will not be received."

    // Activity 2
    const val A2M1 = "This is the client's name for finding it at composition. " +
            "The client will not see this name, but a name or address must be provided to list this client."
    const val A2M2 = "This is the client's address. Should no name be provided the address will be used to find at composition." +
            "The client will not see this address, but a name or address must be provided to list this client."
    const val A2M3 = "This is the client's email. If this field is not blank, the system will send the message by email. " +
            "An email or phone number must be provided to list a client."
    const val A2M4 = "This is the client's phone number. If this field is not blank, the system will send the message by text. " +
            "If both email and phone number are provided, email will be prioritized. An email or phone number must be provided to list a client."

    // Activity 3
    const val A3M1 = "Write the name of your service to be selected at message composition."
    const val A3M2 = "This is the cost of your service, you can also add the same name with a different price."

    // Activity 7
    const val A7M1 = "This will be the email that the system uses for messaging."
    const val A7M2 = "Api keys are needed to perform api calls. \n1: Mailjet Api Key\n2: Mailjet Secret Key\n3: NumVerify Api Key"
}