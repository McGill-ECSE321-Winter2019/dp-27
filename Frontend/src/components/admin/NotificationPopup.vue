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
          filled
          v-model="header"
          label="Notification Title"
          lazy-rules
          :rules="[
            val =>
              (val && val.length > 0) || 'Please enter a notification title'
          ]"
        />
        <div class="text-subtitle2">Body of Notification</div>
        <q-input
          filled
          v-model="message"
          lazy-rules
          autogrow
          :rules="[
            val => (val && val.length > 0) || 'Please enter a notification body'
          ]"
        />
        <q-btn color="primary" type="submit" label="Send Notification" />
        <q-btn flat color="primary" label="Cancel" v-close-popup />
      </q-form>
    </q-card-section>
  </q-card>
</template>

<script>
export default {
  data: function() {
    return {
      header: "",
      message: ""
    };
  },
  created: function() {
    this.header = this.title;
    this.message = this.body;
  },
  props: {
    title: String,
    students: Array,
    body: String
  },
  methods: {
    onSubmit: function() {
      this.students.forEach(s => {
        this.$axios.get("/students/email/" + s.email).then(studentObject => {
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
          this.$axios
            .post("/notifications", body)
            .then(_resp => {
              this.$q.notify({
                color: "green-4",
                position: "top",
                textColor: "white",
                icon: "cloud_done",
                message: "Successfully notified"
              });

              this.$emit("sent");
            })
            .catch(_err => {
              this.$q.notify({
                color: "red-4",
                position: "top",
                textColor: "white",
                icon: "error",
                message: "Something went wrong, please try again"
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
  width: 40%;
}
</style>
