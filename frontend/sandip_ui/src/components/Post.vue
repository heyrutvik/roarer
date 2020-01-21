<template>
    <v-card class="p-10" height="500">
        <v-textarea filled name="input-7-4" label="Filled textarea" height="380" v-model="content">
        </v-textarea>
        <v-card-actions class="justify-center"> 
            <v-form ref="form" @submit.prevent="validateContent">
                <v-tooltip top>
                    <template v-slot:activator="{ on }">
                        <v-btn 
                            v-on="on" 
                            color="primary"
                            rounded
                            raised
                            type="submit"
                            :loading="sendingRequest" 
                            :disabled="sendingRequest"
                        >
                            Validate
                        </v-btn>
                    </template>
                    <span>Click on this to view you thread</span>
                </v-tooltip>
            </v-form>
        </v-card-actions>
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
    name: 'Post',
    data(){
        return{
            content: "Write your full text here...",
            sendingRequest: false,
            snackbar: false,
            error: false,
            snackbarText: 'Something went wrong.',
        }
    },
    computed: {
        snackbarColor() {
            return this.error ? 'error' : 'success'
        }
    },
    methods:{
        validateContent(){
            this.sendingRequest = true
            api.checkThread({content: this.content})
            .then((response) => {
                console.log(response)
                this.thread = response.data
                this.snackbar = true
                this.snackbarText = 'Check your thread'
                this.$emit('updateThread', this.thread)
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

</style>