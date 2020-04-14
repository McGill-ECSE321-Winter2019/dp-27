<template>
  <BasePage title="Companies">
    <q-card flat bordered>
      <div class="q-pa-md">
        <q-table :data="companies" :columns="columns" row-key="id" />
      </div>
    </q-card>
  </BasePage>
</template>

<script>
import BasePage from "../BasePage.vue";

export default {
  name: "AdminCompaniesPage",
  components: {
    BasePage
  },
  data: function() {
    return {
      companies: [],
      loaded: false,
      columns: [
        {
          name: "companyName",
          required: true,
          label: "Company Name",
          align: "left",
          field: "name",
          sortable: true,
          style: "width: 500px"
        },
        {
          name: "City",
          required: true,
          label: "City",
          align: "left",
          field: "city",
          sortable: true,
          style: "width: 500px"
        },
        {
          name: "Region",
          required: true,
          label: "Region",
          align: "left",
          field: "region",
          sortable: true,
          style: "width: 500px"
        },
        {
          name: "Country",
          required: true,
          label: "Country",
          align: "left",
          field: "country",
          sortable: true,
          style: "width: 500px"
        }
      ]
    };
  },

  created: function() {
    this.$axios.get("/companies").then(resp => {
      this.companies = resp.data;
      this.loaded = true;
    });
  }
};
</script>

<style lang="scss" scoped></style>
