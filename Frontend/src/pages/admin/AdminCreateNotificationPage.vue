<template>
  <BasePage title="Send Notification">
    <q-card>
      <div class="q-pa-md">
        <q-table
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
          class="notifPageComponents"
        />
        <q-input
          v-model="body"
          label="Body"
          outlined
          type="textarea"
          class="notifPageComponents"
        />
        <q-btn
          v-if="!sending"
          color="primary"
          @click="sendNotification()"
          label="Send Notification"
          class="notifPageComponents"
        />
        <!-- Show spinner while waiting -->
        <q-spinner
          v-else
          color="primary"
          size="2.5em"
          class="notifPageComponents"
        />
      </div>
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
      required: false
    }
  },
  name: "HomeMainInfo",

  data: () => ({
    sending: false,
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
  }),
  created: function() {
    this.$axios
      .get("/students", {
        headers: {
          Authorization: this.$store.state.token
        }
      })
      .then(resp => {
        this.tableStudents = resp.data;
      });
    this.toSendTo = this.selected;
  },
  methods: {
    goToStudentCoop() {
      this.$router.push("/admin/student-coops");
    },
    sendNotification() {
      var adminId = this.$store.state.currentUser.id;
      var stuIds = [];
      for (var i = 0; i < this.toSendTo.length; i++) {
        stuIds.push(this.toSendTo[i].id);
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
            stuIds
        )
        .then(_resp => {
          this.sending = false;
          this.body = "";
          this.title = "";
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

<style scoped lang="scss">
h6 {
  margin: 10px;
}
#card {
  width: 100%;
  margin-top: 25px;
  margin-right: 10px;
}
.notifPageComponents {
  margin-top: 3%;
}
</style>
