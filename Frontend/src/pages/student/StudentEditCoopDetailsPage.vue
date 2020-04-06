<template>
  <q-page class="flex justify-center">
    <div id="container" class="row">
      <div class="col">
        <h4>Edit Co-op Details</h4>

        <q-card flat bordered class="q-mb-md">
          <!-- Show spinner while submitting -->
          <div v-if="loading" class="center-item q-ma-md">
            <q-spinner color="primary" size="3em" />
          </div>
          <q-form v-else @submit="onSubmit">
            <q-card-section>
              <div class="text-h6 q-mb-sm">Company Information</div>
              <div class="text-caption q-mb-md">
                Please enter the name and location of the company where you will
                be doing your co-op.
              </div>

              <div class="q-gutter-sm">
                <q-select
                  outlined
                  v-model="companyName"
                  use-input
                  hide-selected
                  fill-input
                  input-debounce="0"
                  :options="companyNames"
                  @filter="filterFn"
                  label="Company name"
                  hint="Company name"
                  :rules="[
                    val =>
                      (val && val.length > 0) || 'Please enter a company name'
                  ]"
                >
                  <template v-slot:no-option>
                    <q-item>
                      <q-item-section class="text-grey">
                        No results
                      </q-item-section>
                    </q-item>
                  </template>
                </q-select>

                <q-input
                  outlined
                  v-model="companyCountry"
                  label="Country"
                  hint="Country"
                  :rules="[
                    val => (val && val.length > 0) || 'Please enter a country'
                  ]"
                />
                <q-input
                  outlined
                  v-model="companyRegion"
                  label="Region (e.g. state or province)"
                  hint="Region"
                  :rules="[
                    val => (val && val.length > 0) || 'Please enter a region'
                  ]"
                />
                <q-input
                  outlined
                  v-model="companyCity"
                  label="City"
                  hint="City"
                  :rules="[
                    val => (val && val.length > 0) || 'Please enter a city'
                  ]"
                />
              </div>
            </q-card-section>

            <q-separator inset />

            <q-card-section>
              <div class="text-h6 q-mb-sm">Employer Contact Information</div>
              <div class="text-caption q-mb-md">
                Please provide contact information for an employee at the
                company where you will be doing your co-op. This will usually be
                your manager's contact information.
              </div>

              <div class="row q-col-gutter-md q-mb-md">
                <div class="col-6">
                  <q-input
                    outlined
                    v-model="employerContactFirstName"
                    label="First Name"
                    hint="First Name"
                  />
                </div>
                <div class="col-6">
                  <q-input
                    outlined
                    v-model="employerContactLastName"
                    label="Last Name"
                    hint="Last Name"
                  />
                </div>
              </div>
              <div class="q-gutter-sm">
                <q-input
                  outlined
                  v-model="employerContactEmail"
                  label="Email"
                  hint="Email"
                />

                <q-input
                  outlined
                  v-model="employerContactPhoneNumber"
                  label="Phone Number"
                  hint="Phone Number"
                />
              </div>
            </q-card-section>

            <q-separator inset />

            <q-card-section>
              <div class="text-h6 q-mb-sm">Co-op Information</div>
              <div class="text-caption q-mb-md">
                Please enter the following information for your co-op.
              </div>

              <div class="row q-col-gutter-md q-mb-md">
                <div class="col-6">
                  <q-input
                    outlined
                    v-model="startDate"
                    label="Start date"
                    hint="Start date"
                    :rules="[
                      val =>
                        new Date(endDate) > new Date(startDate) ||
                        (startDate && !endDate) ||
                        'Start date must be before end date'
                    ]"
                  >
                    <template v-slot:append>
                      <q-icon name="event" class="cursor-pointer">
                        <q-popup-proxy
                          ref="qDateProxy"
                          transition-show="scale"
                          transition-hide="scale"
                        >
                          <q-date
                            v-model="startDate"
                            mask="YYYY-MM-DD"
                            @input="() => $refs.qDateProxy.hide()"
                          />
                        </q-popup-proxy>
                      </q-icon>
                    </template>
                  </q-input>
                </div>
                <div class="col-6">
                  <q-input
                    outlined
                    v-model="endDate"
                    label="End date"
                    hint="End date"
                    :rules="[
                      val =>
                        new Date(endDate) > new Date(startDate) ||
                        'End date must be after start date'
                    ]"
                  >
                    <template v-slot:append>
                      <q-icon name="event" class="cursor-pointer">
                        <q-popup-proxy
                          ref="qDateProxy"
                          transition-show="scale"
                          transition-hide="scale"
                        >
                          <q-date
                            v-model="endDate"
                            mask="YYYY-MM-DD"
                            @input="() => $refs.qDateProxy.hide()"
                          />
                        </q-popup-proxy>
                      </q-icon>
                    </template>
                  </q-input>
                </div>
              </div>

              <div class="row q-col-gutter-md q-mb-md">
                <div class="col-6">
                  <q-input
                    outlined
                    v-model="payPerHour"
                    label="Pay Per Hour"
                    hint="Per Per Hour"
                    mask="$#.##"
                    fill-mask="0"
                    reverse-fill-mask
                    input-class="text-right"
                  />
                </div>
                <div class="col-6">
                  <q-input
                    outlined
                    v-model.number="hoursPerWeek"
                    type="number"
                    label="Hours Per Week"
                    hint="Hours Per Week"
                    :rules="[
                      val =>
                        (val && val > 0) || 'Hours per week cannot be negative'
                    ]"
                  />
                </div>
              </div>

              <!-- Disable buttons while submitting -->
              <q-btn
                label="Submit"
                type="submit"
                color="primary"
                :disabled="submitting"
              />
              <!-- Show spinner while submitting -->
              <q-spinner v-if="submitting" color="primary" size="3em" />
            </q-card-section>
          </q-form>
        </q-card>
      </div>
    </div>
  </q-page>
</template>

<script>
export default {
  name: "StudentEditCoopDetailsPage",
  data: function() {
    return {
      coop: null,
      submitting: false,
      loading: true,
      coopId: this.$route.params.coopId,
      companyNames: [],
      companyName: "",
      companyCountry: "",
      companyRegion: "",
      companyCity: "",
      employerContactFirstName: "",
      employerContactLastName: "",
      employerContactEmail: "",
      employerContactPhoneNumber: "",
      startDate: "",
      endDate: "",
      payPerHour: null,
      hoursPerWeek: null
    };
  },
  created: function() {
    this.getCoop();
  },
  methods: {
    onSubmit: function() {},
    getCoop: function() {
      this.loading = true;
      this.$axios.get(`/coops/${this.coopId}`).then(resp => {
        this.coop = resp.data;
        if (this.coop.coopDetails !== null) {
          // prefill inputs
          const employerContact = this.coop.coopDetails.employerContact;
          this.employerContactFirstName = employerContact.firstName;
          this.employerContactLastName = employerContact.lastName;
          this.employerContactEmail = employerContact.email;
          this.employerContactPhoneNumber = employerContact.phoneNumber;

          const company = this.coop.coopDetails.employerContact.company;
          this.companyName = company.name;
          this.companyCountry = company.country;
          this.companyRegion = company.region;
          this.companyCity = company.city;
        }
        this.loading = false;
      });
    },
    filterFn: function(val, update, abort) {
      update(() => {
        const needle = val.toLowerCase();
        this.options = stringOptions.filter(
          v => v.toLowerCase().indexOf(needle) > -1
        );
      });
    }
  }
};
</script>

<style lang="scss" scoped>
#container {
  width: 75%;
}

.center-item {
  text-align: center;
}
</style>
