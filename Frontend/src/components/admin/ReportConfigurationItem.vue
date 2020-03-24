<!-- This component represents a single report configuration.

Parent: AdminReportConfigPage.vue -->
<template>
  <q-card flat bordered class="q-mb-md">
    <q-card-section>
      <div class="text-h6 q-mb-md">{{ type }}</div>

      <div class="text-subtitle1">
        <span class="text-subtitle1 text-weight-medium">
          Requires file (PDF):
        </span>
        {{ requiresFile ? "Yes" : "No" }}
      </div>

      <div class="text-subtitle1">
        <span class="text-subtitle1 text-weight-medium">Deadline:</span>
        {{ deadline }} days from
        {{ isDeadlineFromStart ? "start of co-op" : "end of co-op" }}
      </div>

      <!-- Report section configs -->
      <div class="text-subtitle1 q-mb-md">
        <div class="text-subtitle1 text-weight-medium q-mb-md">
          Report section configurations:
        </div>
        <div class="row">
          <div class="col-7">Section Prompt</div>
          <div class="col-3">Response Type</div>
          <div class="col-2">
            <q-btn
              label="+ New"
              color="primary"
              class="align-right"
              flat
              @click="showReportSectionConfigPopup = true"
            />
          </div>
        </div>

        <q-separator class="q-mb-sm" />

        <div v-if="rsConfigs.length > 0">
          <ReportSectionConfigurationItem
            v-for="rsConfig in rsConfigs"
            :key="rsConfig.id"
            :reportSectionConfig="rsConfig"
            @refresh="notifyParent"
          />
        </div>
        <div v-else class="text-body2">
          There are no report section configurations for this report.
        </div>
      </div>

      <q-btn label="Edit" color="primary" flat @click="showEditPopup = true" />
      <q-btn
        label="Remove"
        color="primary"
        flat
        class="q-ml-sm"
        @click="showDeletePopup = true"
      />

      <q-dialog v-model="showEditPopup">
        <ReportConfigurationPopup
          :isEditing="true"
          :id="id"
          :type="type"
          :requiresFile="requiresFile"
          :deadline="deadline"
          :isDeadlineFromStart="isDeadlineFromStart"
          @refresh="notifyParent"
        />
      </q-dialog>

      <q-dialog v-model="showDeletePopup">
        <ReportConfigurationDeletePopup :id="this.id" @refresh="notifyParent" />
      </q-dialog>

      <q-dialog v-model="showReportSectionConfigPopup">
        <ReportSectionConfigurationPopup
          :reportConfigId="this.id"
          :numberOfQuestions="this.rsConfigs.length"
          @refresh="notifyParent"
        />
      </q-dialog>
    </q-card-section>
  </q-card>
</template>

<script>
import ReportConfigurationPopup from "./ReportConfigurationPopup";
import ReportConfigurationDeletePopup from "./ReportConfigurationDeletePopup.vue";
import ReportSectionConfigurationItem from "./ReportSectionConfigurationItem.vue";
import ReportSectionConfigurationPopup from "./ReportSectionConfigurationPopup.vue";

export default {
  name: "ReportConfigurationItem",
  components: {
    ReportConfigurationPopup,
    ReportConfigurationDeletePopup,
    ReportSectionConfigurationItem,
    ReportSectionConfigurationPopup
  },
  data: function() {
    return {
      showEditPopup: false,
      showDeletePopup: false,
      showReportSectionConfigPopup: false,
      editingReportSection: false,
      rsConfigs: []
    };
  },
  props: {
    id: {
      type: Number,
      required: true
    },
    requiresFile: {
      type: Boolean,
      required: true
    },
    deadline: {
      type: Number,
      required: true
    },
    isDeadlineFromStart: {
      type: Boolean,
      required: true
    },
    type: {
      type: String,
      required: true
    },
    reportSectionConfigs: {
      type: Array,
      default: function() {
        return [];
      }
    }
  },
  methods: {
    notifyParent: function() {
      this.$emit("refresh");
    },
    handleReportSectionEdit: function(id) {
      this.editingReportSection = true;
    },
    compareReportSectionConfigs: function(rsc1, rsc2) {
      // used to sort report section configs by question number
      return rsc1.questionNumber - rsc2.questionNumber;
    }
  },
  created: function() {
    this.rsConfigs = this.reportSectionConfigs.sort(
      this.compareReportSectionConfigs
    );
  }
};
</script>

<style lang="scss" scoped>
.align-right {
  float: right;
}
</style>
