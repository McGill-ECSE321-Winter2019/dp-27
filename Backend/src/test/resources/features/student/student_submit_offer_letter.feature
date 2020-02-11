Feature: the Student can submit their offer letter for a new Coop term

    This feature covers a Student's ability to submit an offer letter,
    which is how the Student applies for a new Coop term.

    Scenario: the Student submits their offer letter
        When the Student uploads a copy of their offer letter
        And submits the Coop Details of their Coop term
        Then the offer letter is put up for review by an Admin
