<!-- This component presents coop details information in a table-like list.
Used on the Student homepage. -->
<template>
  <q-list class="q-mb-sm" bordered separator>
    <q-item>
      <q-item-section>
        <q-item-label overline>TERM</q-item-label>
        <q-item-label>{{ term }}</q-item-label>
      </q-item-section>
    </q-item>

    <q-item class="row">
      <q-item-section class="col-6">
        <q-item-label overline>COMPANY</q-item-label>
        <q-item-label>{{
          this.coop.coopDetails.employerContact.company.name
        }}</q-item-label>
      </q-item-section>
      <q-item-section class="col-6">
        <q-item-label overline>EMPLOYER CONTACT</q-item-label>
        <q-item-label>{{ employerContactInfo }}</q-item-label>
      </q-item-section>
    </q-item>

    <q-item class="row">
      <q-item-section class="col-6">
        <q-item-label overline>START DATE</q-item-label>
        <q-item-label>{{ startDate }}</q-item-label>
      </q-item-section>
      <q-item-section class="col-6">
        <q-item-label overline>END DATE</q-item-label>
        <q-item-label>{{ endDate }}</q-item-label>
      </q-item-section>
    </q-item>

    <q-item class="row">
      <q-item-section class="col-6">
        <q-item-label overline>PAY PER HOUR</q-item-label>
        <q-item-label>{{ payPerHour }}</q-item-label>
      </q-item-section>
      <q-item-section class="col-6">
        <q-item-label overline>HOURS PER WEEK</q-item-label>
        <q-item-label>{{ this.coop.coopDetails.hoursPerWeek }}</q-item-label>
      </q-item-section>
    </q-item>
  </q-list>
</template>

<script>
export default {
  name: "CoopDetailsListView",
  props: {
    coop: {
      type: Object,
      required: true
    }
  },
  computed: {
    term: function() {
      const season = this.$common.convertEnumTextToReadableString(
        this.coop.courseOffering.season
      );
      return `${season} ${this.coop.courseOffering.year}`;
    },
    startDate: function() {
      return this.$common.convertSQLDateToReadableString(
        this.coop.coopDetails.startDate
      );
    },
    endDate: function() {
      return this.$common.convertSQLDateToReadableString(
        this.coop.coopDetails.endDate
      );
    },
    employerContactInfo: function() {
      const firstName = this.coop.coopDetails.employerContact.firstName;
      const lastName = this.coop.coopDetails.employerContact.lastName;
      const email = this.coop.coopDetails.employerContact.email;

      return `${firstName} ${lastName} - ${email}`;
    },
    payPerHour: function() {
      return this.$common.convertCentsToDollarAmount(
        this.coop.coopDetails.payPerHour
      );
    }
  }
};
</script>

<style lang="scss" scoped></style>
