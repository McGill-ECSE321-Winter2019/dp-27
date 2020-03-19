<template>
  <q-page class="flex justify-center">
    <div id="container" class="row">
      <div class="col">
        <h4>Report Configuration</h4>

        <q-card flat bordered class="q-mb-md">
          <q-card-section>
            <div class="text-body2">
              Please note that any changes made here will affect all students
              currently doing a co-op term as well as all students who have
              future co-op terms. Co-op terms that have already been completed
              will not be affected.
            </div>
          </q-card-section>
        </q-card>

        <q-btn
          label="Add New"
          color="primary"
          class="q-mb-md"
          @click="showPopup = true"
        />
        <!-- Show spinner while loading -->
        <div v-if="loading" class="center-item">
          <q-spinner color="primary" size="3em" />
        </div>
        <!-- Show actual data -->
        <div v-else>
          <div v-if="reportConfigs.length > 0">
            <ReportConfigurationItem
              v-for="rc in reportConfigs"
              :key="rc.id"
              :id="rc.id"
              :type="rc.type"
              :requiresFile="rc.requiresFile"
              :deadline="rc.deadline"
              :isDeadlineFromStart="rc.isDeadlineFromStart"
              :reportSectionConfigs="rc.reportSectionConfigs"
              @remove-clicked="renderDeletePopup"
            />
          </div>
          <div v-else class="center-item">
            No report configurations to show.
          </div>
        </div>

        <q-dialog v-model="showPopup">
          <ReportConfigurationPopup @refresh="refreshList" />
        </q-dialog>

        <q-dialog v-model="showDeletePopup">
          <ReportConfigurationDeletePopup
            :id="this.deleteId"
            @refresh="refreshList"
          />
        </q-dialog>
      </div>
    </div>
  </q-page>
</template>

<script>
import ReportConfigurationItem from "../../components/admin/ReportConfigurationItem.vue";
import ReportConfigurationPopup from "../../components/admin/ReportConfigurationPopup.vue";
import ReportConfigurationDeletePopup from "../../components/admin/ReportConfigurationDeletePopup.vue";

export default {
  name: "AdminReportConfigPage",
  components: {
    ReportConfigurationItem,
    ReportConfigurationPopup,
    ReportConfigurationDeletePopup
  },
  data: function() {
    return {
      loading: true,
      showPopup: false,
      showDeletePopup: false,
      reportConfigs: [],
      deleteId: null
    };
  },
  created: function() {
    this.$axios.get("/report-configs").then(resp => {
      this.reportConfigs = resp.data;
      this.loading = false;
    });
  },
  methods: {
    refreshList: function() {
      this.showPopup = false;
      this.showDeletePopup = false;
      this.loading = true;
      this.$axios.get("/report-configs").then(resp => {
        this.reportConfigs = resp.data;
        this.loading = false;
      });
    },
    renderDeletePopup: function(id) {
      // id passed from child
      this.deleteId = id;
      // show popup once the ID to delete is set
      this.showDeletePopup = true;
    }
  }
};
</script>

<style lang="scss" scoped>
#container {
  width: 75%;
}

.section {
  max-width: 250px;
}

.center-item {
  text-align: center;
}
</style>
