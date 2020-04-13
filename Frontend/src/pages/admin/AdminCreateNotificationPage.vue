<!-- This page allows an Admin to send a notification to Students they
select. -->
<template>
  <BasePage title="Send Notification">
    <q-card flat bordered class="q-mb-md">
      <q-card-section>
        <q-form @submit="sendNotification">
          <!-- Show spinner while table data is loading -->
          <div v-if="loading" class="center-item">
            <q-spinner color="primary" size="3em"></q-spinner>
          </div>
          <q-table
            v-else
            title="Select Students"
            :data="tableStudents"
            :columns="columns"
            row-key="email"
            selection="multiple"
            :selected.sync="toSendTo"
          />
          <q-input
            outlined
            v-model="title"
            label="Title"
            hint="Title"
            class="q-mt-md"
            :rules="[
              val =>
                (val && val.length > 0) || 'Please enter a notification title'
            ]"
          />
          <q-input
            v-model="body"
            label="Body"
            hint="Body"
            outlined
            type="textarea"
            class="q-mt-md"
            :rules="[
              val =>
                (val && val.length > 0) || 'Please enter a notification body'
            ]"
          />

          <q-btn
            color="primary"
            type="submit"
            label="Send Notification"
            class="q-mt-md"
            :disabled="sending"
          />
          <!-- Show spinner while waiting -->
          <q-spinner
            v-if="sending"
            color="primary"
            size="2.5em"
            class="q-mt-md q-ml-md"
          />
        </q-form>
      </q-card-section>
    </q-card>
  </BasePage>
</template>

<script>
import BasePage from "../BasePage.vue";

export default {
  components: {
    BasePage
  },
  props: {
    selected: {
      type: Array,
      default: function() {
        return [];
      }
    }
  },
  name: "HomeMainInfo",
  data: function() {
    return {
      sending: false,
      loading: true,
      tableStudents: [],
      toSendTo: [],
      body: "",
      title: "",
      selectedString: "Please select a student",
      columns: [
        {
          name: "studentLastName",
          required: true,
          label: "Student Last Name",
          align: "left",
          field: "lastName",
          sortable: true,
          classes: "my-class",
          style: "width: 500px"
        },
        {
          name: "studentFirstName",
          required: true,
          label: "Student First Name",
          align: "left",
          field: "firstName",
          sortable: true,
          classes: "my-class",
          style: "width: 500px"
        },
        {
          name: "studentID",
          required: true,
          label: "Student ID",
          align: "left",
          field: "id",
          sortable: true,
          classes: "my-class",
          style: "width: 500px"
        },
        {
          name: "email",
          required: true,
          label: "Email",
          align: "left",
          field: "email",
          sortable: true,
          classes: "my-class",
          style: "width: 500px"
        }
      ]
    };
  },
  created: function() {
    this.$axios
      .get("/students", {
        headers: {
          Authorization: this.$store.state.token
        }
      })
      .then(resp => {
        this.tableStudents = resp.data;
        this.loading = false;
      });
    this.toSendTo = this.selected;
  },
  methods: {
    goToStudentCoop: function() {
      this.$router.push("/admin/student-coops");
    },
    sendNotification: function() {
      if (this.toSendTo.length === 0) {
        this.$q.notify({
          color: "red-4",
          position: "top",
          textColor: "white",
          icon: "error",
          message: "Please select at least one student"
        });
        return;
      }

      var adminId = this.$store.state.currentUser.id;
      var studentIds = [];
      for (var i = 0; i < this.toSendTo.length; i++) {
        studentIds.push(this.toSendTo[i].id);
      }
      this.sending = true;
      // create the notification
      this.$axios
        .post(
          "/notifications/many?admin=" +
            adminId +
            "&title=" +
            this.title +
            "&body=" +
            this.body +
            "&studentIds=" +
            studentIds
        )
        .then(_resp => {
          this.sending = false;
          this.toSendTo = [];
          this.$q.notify({
            color: "green-4",
            position: "top",
            textColor: "white",
            icon: "cloud_done",
            message: "Notification(s) sent successfully!"
          });
        })
        .catch(_err => {
          this.sending = false;
          this.$q.notify({
            color: "red-4",
            position: "top",
            textColor: "white",
            icon: "error",
            message: "Something went wrong, please try again"
          });
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
