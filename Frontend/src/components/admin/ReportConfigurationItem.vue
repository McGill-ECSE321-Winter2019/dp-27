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
            <q-btn label="+ New" color="primary" class="align-right" flat />
          </div>
        </div>

        <q-separator class="q-mb-sm" />

        <div v-if="rsConfigs.length > 0">
          <ReportSectionConfigurationItem
            v-for="rsConfig in rsConfigs"
            :key="rsConfig.prompt"
            :prompt="rsConfig.prompt"
            :responseType="rsConfig.responseType"
          />
        </div>
        <div v-else class="text-body2">
          There are no report section configurations for this report.
        </div>
      </div>

      <q-btn label="Edit" color="primary" flat />
      <q-btn
        label="Remove"
        color="primary"
        flat
        class="q-ml-sm"
        @click="notifyParent"
      />
    </q-card-section>
  </q-card>
</template>

<script>
import ReportSectionConfigurationItem from "./ReportSectionConfigurationItem.vue";

export default {
  name: "ReportConfigurationItem",
  components: {
    ReportSectionConfigurationItem
  },
  data: function() {
    return {
      rsConfigs: this.reportSectionConfigs
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
      this.$emit("remove-clicked", this.id);
    }
  }
};
</script>

<style lang="scss" scoped>
.align-right {
  float: right;
}
</style>
