Feature: the Student can upload a required Report for their Coop term

    This features covers the various scenarios that a Student can be
    in when they upload a required Report.

    Scenario: Student uploads Report
        Given the Student is currently doing a Coop term
        And the Student has a Report due
        When the Student uploads the proper Report
        Then the Report is saved in the system

    Scenario: Student overwrites previous Report upload
        Given the Student is currently doing a Coop term
        And the Student has uploaded a Report type previously
        When the Student uploads the same type of Report again
        Then the new Report overwrites the old one in the system