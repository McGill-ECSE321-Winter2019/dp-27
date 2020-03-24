<template>
    <q-page class="flex justify-center">
        <div id="container" class="row">
            <div class="col">
                <h4>Check Enrollment</h4>
                    <div class="text-body2">
                        Select the semester that you would like to check the students enrollment 
                        in their respective courses or if they have created a corresponding coop yet
                    </div>
                <q-select v-model="selectedYear" :options="yearList" label="Select Year" />
                <q-select v-model="selectedSeason" :options="seasonList" label="Select Season" />
                <q-select v-model="selectedCourse" :options="courseList" label="Select Course" />
                <q-file v-model="csvFile" label="Upload Course CSV" rounded accept=".csv" />
                <q-btn class="q-mt-md" color="primary" label="Check Students" :loading="uploading" @click="parseCsv"/>
            </div>
        </div>

        <div id="container" class="row flex justify-center">
            <div class="col">
                <q-card flat bordered class="q-mb-md">
                    <q-card-section>
                        <div class="text-h6 q-mb-md">Students who have not enrolled</div>
                    </q-card-section>
                    <div v-if="unenrolledStudents.length > 0" class="q-ml-md">
                        <li v-for="student in unenrolledStudents" :key="student">
                            {{ student }}
                        </li>
                    </div>
                    <q-card-actions vertical align="right">
                        <q-btn @click="notifyEnrolled" flat>Notify all</q-btn>
                        <q-btn @click="copyEnrolled" flat>Copy List</q-btn>
                    </q-card-actions>
                </q-card>

                <q-card flat bordered class="q-mb-md">
                    <q-card-section>
                        <div class="text-h6 q-mb-md">Students who have not created a new Co-op</div>
                    </q-card-section>
                    <div v-if="unregisteredStudents.length > 0" class="q-ml-md">
                        <li v-for="student in unregisteredStudents" :key="student">
                            {{ student }}
                        </li>
                    </div>
                    <q-card-actions vertical align="right">
                        <q-btn @click="copyRegistered" flat>Copy List</q-btn>
                    </q-card-actions>
                </q-card>
            </div>
        </div>    
    </q-page>
</template>

<script>
export default {
    name: "CSVParserPage",

    data: function() {
        return {
            selectedYear: "",
            yearList: [],
            seasonList: [],
            selectedSeason: "",
            courseList: [],
            selectedCourse: "",
            unenrolledStudents: [],
            unregisteredStudents: [],
            csvFile: null,
            uploading: false,
            courseOfferingId: ""

        };
    },
    created: function() {
        this.$axios.get("/courses").then(resp => {
            resp.data.forEach(course => {
                this.courseList.push(course.name);
            });
        });
        this.$axios.get("/season").then(resp => {
            resp.data.forEach(season => {
                this.seasonList.push(season);
            });
        });
        this.$axios.get("/course-offerings/years").then(resp => {
            resp.data.forEach(year => {
                this.yearList.push(year);
            });
        });
    },  
    methods: {
        parseCsv: function(){
            this.uploading = true;

            this.$axios.get("/course-offerings/single-offering", {
                params: {
                    year: this.selectedYear,
                    season: this.selectedSeason,
                    courseName: this.selectedCourse
                }
            }).then(resp => {
                this.courseOfferingId = resp.data.id;
                const formData = new FormData();
                formData.append("file", this.csvFile);
                formData.append("course_id", this.courseOfferingId);
            
                this.$axios.post("/csv-parser/check-registered",  formData, {
                  headers: { "Content-Type": "multipart/form-data" }
                }).then(resp => {
                    this.unregisteredStudents = resp.data;
                });

                this.$axios.post("/csv-parser/check-enrollment",  formData, {
                  headers: { "Content-Type": "multipart/form-data" }
                }).then(resp => {
                    this.unenrolledStudents = resp.data;
                });

                this.uploading = false;
                });
        },
        copyEnrolled: function(){
            
        },
        copyRegistered: function(){

        },
        notifyEnrolled: function(){
            unenrolledStudents.forEach(email => {
                this.$axios.get("/students", {
                    params: {
                        "email": email
                    }
                }).then(resp => {
                    this.$axios.post()
                })
            })
        }
    }
}
</script>

<style lang="scss" scoped>
#container {
  width: 60%;
}

.section {
  max-width: 100%;
}

.center-item {
  text-align: center;
}
</style>
