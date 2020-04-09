<!-- This components shows a Student's CoopDetails but does not allow editing,
since they have already submitted. -->
<template>
  <div>
    <!-- Employer Contact section -->
    <q-card-section>
      <div class="text-h6 q-mb-sm">Employer Contact Information</div>
      <div class="text-caption q-mb-md">
        Enter your manager's email to see if their information exists in the
        system. If not, you will need to enter the appropriate information.
      </div>

      <div class="row q-col-gutter-md q-mb-md">
        <div class="col-6">
          <q-input
            outlined
            v-model="coopDetails.employerContact.firstName"
            label="First Name"
            hint="First Name"
            :disable="true"
          />
        </div>
        <div class="col-6">
          <q-input
            outlined
            v-model="coopDetails.employerContact.lastName"
            label="Last Name"
            hint="Last Name"
            :disable="true"
          />
        </div>
      </div>
      <div class="q-gutter-sm">
        <q-input
          outlined
          v-model="coopDetails.employerContact.email"
          label="Email"
          hint="Email"
          :disable="true"
        />

        <q-input
          outlined
          v-model="coopDetails.employerContact.phoneNumber"
          label="Phone Number"
          hint="Phone Number"
          :disable="true"
        />
      </div>
    </q-card-section>

    <q-separator inset />

    <!-- Company section -->
    <q-card-section>
      <div class="text-h6 q-mb-sm">Company Information</div>
      <div class="text-caption q-mb-md">
        Please enter the name and location of the company where you will be
        doing your co-op.
      </div>

      <div class="q-gutter-sm">
        <q-select
          outlined
          :value="coopDetails.employerContact.company.name"
          use-input
          hide-selected
          fill-input
          input-debounce="0"
          label="Company name"
          hint="Company name"
          :disable="true"
        />
        <q-select
          outlined
          :value="coopDetails.employerContact.company.country"
          use-input
          hide-selected
          fill-input
          input-debounce="0"
          label="Country"
          hint="Country"
          :disable="true"
        />
        <q-select
          outlined
          :value="coopDetails.employerContact.company.region"
          use-input
          hide-selected
          fill-input
          input-debounce="0"
          label="Region (e.g. state or province)"
          hint="Region"
          :disable="true"
        />
        <q-select
          outlined
          :value="coopDetails.employerContact.company.city"
          use-input
          hide-selected
          fill-input
          input-debounce="0"
          label="City"
          hint="City"
          :disable="true"
        />
      </div>
    </q-card-section>

    <q-separator inset />

    <!-- Coop section -->
    <q-card-section>
      <div class="text-h6 q-mb-sm">Co-op Information</div>
      <div class="text-caption q-mb-md">
        Please enter the following information for your co-op.
      </div>

      <div class="row q-col-gutter-md q-mb-md">
        <div class="col-6">
          <q-input
            outlined
            v-model="coopDetails.startDate"
            label="Start date"
            hint="Start date"
            :disable="true"
          />
        </div>
        <div class="col-6">
          <q-input
            outlined
            v-model="coopDetails.endDate"
            label="End date"
            hint="End date"
            :disable="true"
          />
        </div>
      </div>

      <div class="row q-col-gutter-md q-mb-md">
        <div class="col-6">
          <q-input
            outlined
            v-model="coopDetails.payPerHour"
            label="Pay Per Hour"
            hint="Per Per Hour"
            mask="#.##"
            fill-mask="0"
            prefix="$"
            reverse-fill-mask
            input-class="text-right"
            :disable="true"
          />
        </div>
        <div class="col-6">
          <q-input
            outlined
            v-model.number="coopDetails.hoursPerWeek"
            type="number"
            label="Hours Per Week"
            hint="Hours Per Week"
            :disable="true"
          />
        </div>
      </div>
    </q-card-section>
  </div>
</template>

<script>
export default {
  name: "StudentCoopDetailsPageExistingCoopDetails",
  props: {
    coop: {
      type: Object,
      required: true
    }
  },
  computed: {
    coopDetails: function() {
      return this.coop.coopDetails;
    }
  }
};
</script>

<style lang="scss" scoped></style>
