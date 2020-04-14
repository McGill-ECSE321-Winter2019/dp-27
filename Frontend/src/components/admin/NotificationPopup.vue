<template>
  <q-card id="card">
    <q-card-section class="row items-center">
      <div class="text-h6">Send Notification</div>
      <q-space />
      <q-btn icon="close" flat round dense v-close-popup />
    </q-card-section>
    <q-card-section>
      <q-form @submit="onSubmit" class="q-gutter-sm">
        <q-input
          outlined
          v-model="header"
          label="Notification Title"
          hint="Notification Title"
          lazy-rules
          :rules="[
            val =>
              (val && val.length > 0) || 'Please enter a notification title'
          ]"
        />
        <q-input
          outlined
          v-model="body"
          lazy-rules
          type="textarea"
          label="Notification Body"
          hint="Notification Body"
          :rules="[
            val =>
              (val && val.length > 0) || 'Please enter a notification title'
          ]"
        />
        <q-btn color="primary" type="submit" label="Send" v-close-popup />
      </q-form>
    </q-card-section>
  </q-card>
</template>

<script>
export default {
  data: function() {
    return {
      header: "",
      body: ""
    };
  },
  props: {
    title: String,
    students: Array,
    course: Object
  },
  created: function() {
    this.header = "Reminder to Register for " + this.title;
  },
  methods: {
    onSubmit: function() {
      this.students.forEach(s => {
        this.$axios.get("/students/email/" + s).then(studentObject => {
          const body = {
            title: this.header,
            body: this.body,
            student: {
              id: studentObject.data.id
            },
            sender: {
              id: this.$store.state.currentUser.id
            }
          };
          this.$axios.post("/notifications", body).then(_resp => {
            this.$q.notify({
              color: "green-4",
              position: "top",
              textColor: "white",
              icon: "cloud_done",
              message: "Created Successfully"
            });
          });
        });
      });
    }
  }
};
</script>

<style lang="scss" scoped>
#card {
  width: 45%;
}
</style>
