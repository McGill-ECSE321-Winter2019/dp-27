Feature: the Admin can approve or deny a Student's Coop term

    This feature covers the Admin's ability to approve or deny a Student's
    Coop term.

    Scenario: Admin approves Coop term
    		Given a Student has a coop they would like to register
        When the Student submits their offer letter
        Then the Admin approves the Coop term

    Scenario: Admin denies Coop term
    		Given a Student has a coop they would like to register
        When the Student submits their offer letter
        Then the Admin rejects the Coop term
