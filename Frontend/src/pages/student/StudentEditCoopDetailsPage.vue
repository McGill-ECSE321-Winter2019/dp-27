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
            <!-- Company section -->
            <q-card-section>
              <div class="text-h6 q-mb-sm">Company Information</div>
              <div class="text-caption q-mb-md">
                Please enter the name and location of the company where you will
                be doing your co-op.
              </div>

              <div class="q-gutter-sm">
                <q-select
                  outlined
                  :value="companyName"
                  use-input
                  hide-selected
                  fill-input
                  input-debounce="0"
                  :options="companyNames"
                  @filter="filterCompanyNames"
                  @input-value="setCompanyName"
                  label="Company name"
                  hint="Company name"
                  :rules="[
                    val =>
                      (val && val.length > 0) || 'Please enter a company name'
                  ]"
                />

                <q-select
                  outlined
                  :value="companyCountry"
                  use-input
                  hide-selected
                  fill-input
                  input-debounce="0"
                  :options="countries"
                  @filter="filterCompanyCountries"
                  @input-value="setCompanyCountry"
                  label="Country"
                  hint="Country"
                  :rules="[
                    val => (val && val.length > 0) || 'Please enter a country'
                  ]"
                />
                <q-select
                  outlined
                  :value="companyRegion"
                  use-input
                  hide-selected
                  fill-input
                  input-debounce="0"
                  :options="regions"
                  @filter="filterCompanyRegions"
                  @input-value="setCompanyRegion"
                  label="Region (e.g. state or province)"
                  hint="Region"
                  :rules="[
                    val => (val && val.length > 0) || 'Please enter a region'
                  ]"
                />
                <q-select
                  outlined
                  :value="companyCity"
                  use-input
                  hide-selected
                  fill-input
                  input-debounce="0"
                  :options="cities"
                  @filter="filterCompanyCities"
                  @input-value="setCompanyCity"
                  label="City"
                  hint="City"
                  :rules="[
                    val => (val && val.length > 0) || 'Please enter a city'
                  ]"
                />
              </div>
            </q-card-section>

            <q-separator inset />
            <!-- Employer Contact section -->
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
                    :rules="[
                      val =>
                        (val && val.length > 0) || 'Please enter a first name'
                    ]"
                  />
                </div>
                <div class="col-6">
                  <q-input
                    outlined
                    v-model="employerContactLastName"
                    label="Last Name"
                    hint="Last Name"
                    :rules="[
                      val =>
                        (val && val.length > 0) || 'Please enter a last name'
                    ]"
                  />
                </div>
              </div>
              <div class="q-gutter-sm">
                <q-input
                  outlined
                  v-model="employerContactEmail"
                  label="Email"
                  hint="Email"
                  :rules="[
                    val => (val && val.length > 0) || 'Please enter an email'
                  ]"
                />

                <q-input
                  outlined
                  v-model="employerContactPhoneNumber"
                  label="Phone Number"
                  hint="Phone Number"
                  :rules="[
                    val =>
                      (val && val.length > 0) || 'Please enter a phone number'
                  ]"
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
                    mask="#.##"
                    fill-mask="0"
                    prefix="$"
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
                class="q-mr-md"
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
      countries: [],
      regions: [],
      cities: [],
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
      payPerHour: "",
      hoursPerWeek: null
    };
  },
  created: function() {
    this.getCoop();
  },
  methods: {
    onSubmit: function() {
      this.submitting = true;

      const body = {
        coop: this.coop,
        startDate: this.startDate,
        endDate: this.endDate,
        payPerHour: parseFloat(this.payPerHour) * 100,
        hoursPerWeek: this.hoursPerWeek,
        employerContact: {
          email: this.employerContactEmail,
          phoneNumber: this.employerContactPhoneNumber,
          firstName: this.employerContactFirstName,
          lastName: this.employerContactLastName,
          company: {
            name: this.companyName,
            country: this.companyCountry,
            region: this.companyRegion,
            city: this.companyCity
          }
        }
      };

      if (this.coop.coopDetails === null) {
        // create the coop details
        this.$axios.post("/coop-details", body).then(resp => {
          this.coop.coopDetails = resp.data;

          this.$q.notify({
            color: "green-4",
            position: "top",
            textColor: "white",
            icon: "cloud_done",
            message: "Updated Successfully"
          });

          this.submitting = false;
        });
      } else {
        // update the coop details
        const coopDetailsId = this.coop.coopDetails.id;
        this.$axios.put(`/coop-details/${coopDetailsId}`, body).then(resp => {
          this.coop.coopDetails = resp.data;

          this.$q.notify({
            color: "green-4",
            position: "top",
            textColor: "white",
            icon: "cloud_done",
            message: "Updated Successfully"
          });

          this.submitting = false;
        });
      }
    },
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

          this.startDate = this.coop.coopDetails.startDate;
          this.endDate = this.coop.coopDetails.endDate;
          this.payPerHour = parseFloat(
            this.coop.coopDetails.payPerHour / 100
          ).toFixed(2);
          this.hoursPerWeek = this.coop.coopDetails.hoursPerWeek;
        }
        this.loading = false;
      });
    },
    getCompaniesInfo: function() {
      // this is used in the Company section to let the student filter
      // by existing options
      this.$axios.get("/companies").then(resp => {
        let namesSet = new Set();
        let countriesSet = new Set();
        let regionsSet = new Set();
        let citiesSet = new Set();
        namesSet.add("");
        countriesSet.add("");
        regionsSet.add("");
        citiesSet.add("");

        resp.data.forEach(company => {
          namesSet.add(company.name);
          countriesSet.add(company.country);
          regionsSet.add(company.region);
          citiesSet.add(company.city);
        });

        this.companyNames = Array.from(namesSet);
        this.countries = Array.from(countriesSet);
        this.regions = Array.from(regionsSet);
        this.cities = Array.from(citiesSet);
      });
    },
    setCompanyName: function(val) {
      this.companyName = val;
    },
    setCompanyCountry: function(val) {
      this.companyCountry = val;
    },
    setCompanyRegion: function(val) {
      this.companyRegion = val;
    },
    setCompanyCity: function(val) {
      this.companyCity = val;
    },
    filterCompanyNames: function(val, update, abort) {
      update(() => {
        const needle = val.toLowerCase();
        this.companyNames = this.companyNames.filter(
          v => v.toLowerCase().indexOf(needle) > -1
        );
      });
    },
    filterCompanyCountries: function(val, update, abort) {
      update(() => {
        const needle = val.toLowerCase();
        this.countries = this.countries.filter(
          v => v.toLowerCase().indexOf(needle) > -1
        );
      });
    },
    filterCompanyRegions: function(val, update, abort) {
      update(() => {
        const needle = val.toLowerCase();
        this.regions = this.regions.filter(
          v => v.toLowerCase().indexOf(needle) > -1
        );
      });
    },
    filterCompanyCities: function(val, update, abort) {
      update(() => {
        const needle = val.toLowerCase();
        this.cities = this.cities.filter(
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
