import Vuex from 'vuex'
import Vue from 'vue'

Vue.use(Vuex)
const store = new Vuex.Store({
    state: {
        token: "",
        user:null
    },
       mutations:{
         changeToken(state,token){
             console.log("store:",token)
            state.token=token
         },
         setUser(state,user){
            state.user = user
         }
       }
   })


   export default store