import Vuex from 'vuex'
import Vue from 'vue'

Vue.use(Vuex)
const store = new Vuex.Store({
    state: {
        token: "",
        user:null,
        messages:[{
         "content":"xdj",
         "id":25,
         "insertTime":"2021-09-24 22:14:32",
         "path":"https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png",
         "path1":"https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png",
         "path2":null,
         "title":"first",
         "type":"png"
     },
     {
      "content":"xdj26",
      "id":26,
      "insertTime":"2021-09-24 22:22:32",
      "path":"https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png",
      "path1":"https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png",
      "path2":"https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png",
      "title":"second",
      "type":"png"
  }],
        messageCount: 0,
    },
       mutations:{
         changeToken(state,token){
            console.log("store:",token)
            state.token=token
         },
         setUser(state,user){
            state.user = user
         },
         addMessage(state,msg){
            state.messages.unshift(msg.data)
         },
         addMessageCount(state){
            state.messageCount = state.messageCount+1
         }
       }
   })


   export default store