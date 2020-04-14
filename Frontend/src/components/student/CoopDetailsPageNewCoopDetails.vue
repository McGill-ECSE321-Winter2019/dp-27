<template>
  <q-form @submit="onSubmit">
    <!-- Employer Contact section -->
    <q-card-section>
      <div class="text-h6 q-mb-sm">Employer Contact Information</div>
      <div class="text-caption q-mb-md">
        Enter your manager's email to see if their information exists in the
        system. If not, you will need to enter the appropriate information.
      </div>

      <div class="row q-col-gutter-md q-mb-md">
        <div class="col-6">
          <q-input outlined v-model="employerContactEmail" type="search">
            <template v-slot:append>
              <q-icon name="search" />
            </template>
          </q-input>
        </div>
      </div>

      <div class="q-mb-md">
        <q-btn
          color="primary"
          label="Search"
          class="q-mr-md"
          :disabled="isSearching"
          @click="searchForEmployerContact"
        />
        <!-- Show spinner while searching -->
        <q-spinner v-if="isSearching" color="primary" size="2.5em" />
      </div>

      <div v-if="searched">
        <!-- Show a warning that no EmployerContact was found -->
        <q-banner
          v-if="employerContact === null"
          inline-actions
          rounded
          class="bg-orange-7 text-white q-mb-md"
        >
          We could not find an employer contact with that email. Please provide
          contact information for an employee at the company where you will be
          doing your co-op.
          <template v-slot:avatar>
            <q-icon name="warning" color="white" />
          </template>
        </q-banner>
        <!-- Show success banner otherwise -->
        <q-banner
          v-else
          inline-actions
          rounded
          class="bg-green-4 text-white q-mb-md"
        >
          Employer contact found! Their information has been pre-filled below.
          <template v-slot:avatar>
            <q-icon name="cloud_done" color="white" />
          </template>
        </q-banner>

        <div class="row q-col-gutter-md q-mb-md">
          <div class="col-6">
            <q-input
              outlined
              v-model="employerContactFirstName"
              label="First Name"
              hint="First Name"
              :disable="disableInput"
              :rules="[
                val => (val && val.length > 0) || 'Please enter a first name'
              ]"
            />
          </div>
          <div class="col-6">
            <q-input
              outlined
              v-model="employerContactLastName"
              label="Last Name"
              hint="Last Name"
              :disable="disableInput"
              :rules="[
                val => (val && val.length > 0) || 'Please enter a last name'
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
            :disable="disableInput"
            :rules="[val => (val && val.length > 0) || 'Please enter an email']"
          />

          <q-input
            outlined
            v-model="employerContactPhoneNumber"
            label="Phone Number"
            hint="Phone Number"
            :disable="disableInput"
            :rules="[
              val => (val && val.length > 0) || 'Please enter a phone number'
            ]"
          />
        </div>
      </div>
    </q-card-section>

    <q-separator inset />
    <div v-if="searched">
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
            :disable="disableInput"
            :rules="[
              val => (val && val.length > 0) || 'Please enter a company name'
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
            :disable="disableInput"
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
            :disable="disableInput"
            :rules="[val => (val && val.length > 0) || 'Please enter a region']"
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
            :disable="disableInput"
            :rules="[val => (val && val.length > 0) || 'Please enter a city']"
          />
        </div>
      </q-card-section>

      <q-separator inset />
    </div>

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
              val => (val && val > 0) || 'Hours per week cannot be negative'
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
        :disabled="submitting || !searched"
      />
      <!-- Show spinner while submitting -->
      <q-spinner v-if="submitting" color="primary" size="2.5em" />
    </q-card-section>

    <q-dialog v-model="showConfirmationPopup">
      <NewCoopDetailsViewConfirmPopup
        @create-coop-details="createCoopDetails"
      />
    </q-dialog>
  </q-form>
</template>

<script>
import NewCoopDetailsViewConfirmPopup from "./NewCoopDetailsViewConfirmPopup.vue";

export default {
  name: "StudentCoopDetailsPageNewCoopDetails",
  components: {
    NewCoopDetailsViewConfirmPopup
  },
  props: {
    coop: {
      type: Object,
      required: true
    }
  },
  data: function() {
    return {
      submitting: false,
      disableInput: false,
      isSearching: false,
      searched: false,
      showConfirmationPopup: false,
      companyNames: [],
      countries: [],
      regions: [],
      cities: [],
      companyName: "",
      companyCountry: "",
      companyRegion: "",
      companyCity: "",
      employerContact: null,
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
    this.getCompaniesInfo();
  },
  methods: {
    onSubmit: function() {
      this.showConfirmationPopup = true;
    },
    createCoopDetails: function() {
      this.showConfirmationPopup = false;
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
    getCompaniesInfo: function() {
      // this is used in the Company section to let the student filter
      // by existing options
      this.$axios.get("/companies").then(resp => {
        let namesSet = new Set();
        let countriesSet = new Set();
        let regionsSet = new Set();
        let citiesSet = new Set();

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
    // checks if an EmployerContact exists and updates the form inputs
    // accordingly
    searchForEmployerContact: function() {
      this.isSearching = true;
      this.searched = false;
      this.employerContact = null;
      this.disableInput = false;

      this.employerContactFirstName = null;
      this.employerContactLastName = null;
      this.employerContactPhoneNumber = null;

      this.companyName = null;
      this.companyCountry = null;
      this.companyRegion = null;
      this.companyCity = null;

      this.$axios
        .get(`/employer-contacts/email/${this.employerContactEmail}`)
        .then(resp => {
          this.employerContact = resp.data;
          this.isSearching = false;
          this.searched = true;
          this.disableInput = true;

          this.employerContactFirstName = this.employerContact.firstName;
          this.employerContactLastName = this.employerContact.lastName;
          this.employerContactEmail = this.employerContact.email;
          this.employerContactPhoneNumber = this.employerContact.phoneNumber;

          this.companyName = this.employerContact.company.name;
          this.companyCountry = this.employerContact.company.country;
          this.companyRegion = this.employerContact.company.region;
          this.companyCity = this.employerContact.company.city;
        })
        .catch(_err => {
          // EmployerContact doesn't exist
          this.isSearching = false;
          this.searched = true;
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
.center-item {
  text-align: center;
}
</style>
