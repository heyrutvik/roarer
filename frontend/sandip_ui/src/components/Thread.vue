<template>
    <v-card class="p-10 overflow-y" height="500">
            <div v-if="threads.length > 0">
                <v-timeline :reverse="reverse" dense>
                    <v-timeline-item
                        v-for="(p, index) in threads" :key="index"
                    >
                        <v-card class="elevation-2">
                        <v-card-text>
                            {{p}}
                        </v-card-text>
                        </v-card>
                    </v-timeline-item>
                </v-timeline>
            </div>
            <v-alert prominent type="success" v-if="threads.length > 0">
                <v-row align="center">
                    <v-col class="grow">Are you want to tweet it?</v-col>
                    <v-col class="shrink">
                    <v-btn color="primary" 
                        @click="publish"
                        :loading="sendingRequest" 
                        :disabled="sendingRequest"    
                    >YES</v-btn>
                    </v-col>
                </v-row>
            </v-alert>
            <v-snackbar
                v-model="snackbar"
                :color="snackbarColor"
                multi-line
                dark
                bottom
            >
            {{ snackbarText }}
                <v-btn text @click="snackbar = false">
                    Close
                </v-btn>
            </v-snackbar>
    </v-card>
</template>

<script>
import api from '@apis/api'

export default {
    name: 'Thread',
    data(){
        return{
            sendingRequest: false,
            snackbar: false,
            error: false,
            snackbarText: 'Something went wrong.',
            reverse: false,
        }
    },
    props: {
        threads: Array
    },
    computed: {
        snackbarColor() {
            return this.error ? 'error' : 'success'
        }
    },
    methods:{
        publish(){
            this.sendingRequest = true
            api.publish({thread: this.threads})
            .then((response) => {
                console.log(response);
                this.snackbar = true
                this.snackbarText = 'Woohoo your tweet as been posted'
                this.$emit("updateThread")
            })
            .catch((error) => {
                this.snackbarText = error.response.statusText
                this.snackbar = true
                this.error = true
            })
            .finally(() => {
                this.sendingRequest = false
            })
        }
    }
}
</script>

<style scoped>
.overflow-y{overflow-y: scroll;}
</style>