Feature: the Student can get the information for their Coop terms

    This feature covers the functionality of the backend which returns
    the Coop information for a particular Student.

    Scenario: Student gets information for their Coop term(s)
        Given the Student has at least one current or previous Coop term
        When the Student requests to get their Coop term information
        Then the system returns the information for all of their Coop terms