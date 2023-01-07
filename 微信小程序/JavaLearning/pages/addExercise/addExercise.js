// pages/addExercise/addExercise.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        ksAndKPList:[],
        moduleList:[],
        moduleIDs:[],
        moduleNames:[],
        ksNames:[],
        exerciseTypes:['选择题','判断题','填空题'],
        choiceOptionCounts:[2,3,4,5,6,7,8],
        choiceOptionKeyCounts:[],
        selectedExericseType:'',
        selectedOptionCount:0,
        selectChoiceKey:0,
        choiceOptionContents:[],
        choiceOptionContentsBuffer:[],
        judgmentOptionKeys:['true','false'],
        selectedModuleID:'',
        selectedModuleName:'',
        selectedKSID:'',
        selectedKSIDIndex:0,
        selectedKSName:'',
        selectedKPName:'',
        stemVal:'',
        addExerciseConfirm:false
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (choiceOptions) {
        for(var i=0;i<2;i++){
            this.data.choiceOptionContents[i]=''
            this.data.choiceOptionKeyCounts[i]=i+1
        }
        this.setData({
            student_ID:getApp().globalData.student_ID,
            moduleAndKSList:[],
            selectedExerciseType:this.data.exerciseTypes[0],
            selectedOptionCount:this.data.choiceOptionCounts[0],
            choiceOptionContents:this.data.choiceOptionContents,
            choiceOptionKeyCounts:this.data.choiceOptionKeyCounts,
            selectedChoiceKey:this.data.choiceOptionKeyCounts[0],
            selectedJudgmentKey:this.data.judgmentOptionKeys[0]
        })
        var that=this
        wx.request({
            url: 'http://'+getApp().globalData.ip+':8080/getModules',
            data:{
            },
            header: {
                'content-type': 'application/json' // 默认值
            },
            success: function (res) { 
                that.setData({
                    selectedModuleID:res.data[0].moduleID,
                    selectedModuleName:res.data[0].moduleName
                })
                for(var i=0;i<res.data.length;i++){
                    that.data.moduleIDs[i]=res.data[i].moduleID,
                    that.data.moduleNames[i]=res.data[i].moduleName
                    var newarray=[{
                        id:i+1,
                        moduleID:res.data[i].moduleID,
                        moduleName:res.data[i].moduleName,
                    }]
                    that.setData({
                        moduleIDs:that.data.moduleIDs,
                        moduleNames:that.data.moduleNames,
                        moduleAndKSList:that.data.moduleAndKSList.concat(newarray)
                    })
                }
                that.setData({
                    selectedChoiceKey:that.data.choiceOptionKeyCounts[0],
                })
                that.renewKSAndKP()
            }
        })
    },

    /**
     * 生命周期函数--监听页面初次渲染完成
     */
    onReady: function () {

    },

    /**
     * 生命周期函数--监听页面显示
     */
    onShow: function () {

    },

    /**
     * 生命周期函数--监听页面隐藏
     */
    onHide: function () {

    },

    /**
     * 生命周期函数--监听页面卸载
     */
    onUnload: function () {

    },

    /**
     * 页面相关事件处理函数--监听用户下拉动作
     */
    onPullDownRefresh: function () {

    },

    /**
     * 页面上拉触底事件的处理函数
     */
    onReachBottom: function () {

    },

    /**
     * 用户点击右上角分享
     */
    onShareAppMessage: function () {

    },


    mapKSID:function(k){
        for(var i=0;i<this.data.ksAndKPList.length;i++){
            if(this.data.ksAndKPList[i].ksID==k) return i
        }
        return null
    },

    bindModulePickerChange(e) {
        console.log('picker发送选择改变，携带值为', this.data.moduleAndKSList[e.detail.value].moduleID);
        this.setData({
            selectedModuleID:this.data.moduleIDs[e.detail.value],
            selectedModuleName:this.data.moduleNames[e.detail.value]
        })
        this.renewKSAndKP()
    },

    renewKSAndKP:function(){
        this.setData({
            ksAndKPList:[],
            ksNames:[],
        })
        var that=this
        wx.request({
            url: 'http://'+getApp().globalData.ip+':8080/getKnowledgeSetsAndPoints',
            data:{
                student_ID:that.data.student_ID,
                moduleID:that.data.selectedModuleID
            },
            header: {
                'content-type': 'application/json' // 默认值
            },
            success: function (res) { 
                that.setData({
                    selectedKSID:res.data[0].ksID[0],
                    selectedKSName:res.data[0].ksName[0]
                })
                for(var i=0;i<res.data.length;i++){
                    that.data.ksNames[i]=res.data[i].ksName[0]
                    var newarray=[{
                        id:i+1,
                        ksID:res.data[i].ksID[0],
                        ksName:res.data[i].ksName[0],
                        kpIDs:res.data[i].kpIDs,
                        kpNames:res.data[i].kpNames,
                    }]
                    that.setData({
                        ksNames:that.data.ksNames,
                        ksAndKPList:that.data.ksAndKPList.concat(newarray)
                    })
                }
                that.setData({
                    selectedKPID:that.data.ksAndKPList[0].kpIDs[0],
                    selectedKPName:that.data.ksAndKPList[0].kpNames[0]
                })
            }
        })
    },

    bindKSPickerChange(e) {
        this.setData({
            selectedKSID:this.data.ksAndKPList[e.detail.value].ksID,
            selectedKSIDIndex:this.mapKSID(this.data.ksAndKPList[e.detail.value].ksID),
            selectedKSName:this.data.ksAndKPList[e.detail.value].ksName
        })
        this.setData({
            selectedKPID:this.data.ksAndKPList[this.data.selectedKSIDIndex].kpIDs[0],
            selectedKPName:this.data.ksAndKPList[this.data.selectedKSIDIndex].kpNames[0]
        })
    },

    bindKPPickerChange(e) {
        this.setData({
            selectedKPID:this.data.ksAndKPList[this.data.selectedKSIDIndex].kpIDs[e.detail.value],
            selectedKPName:this.data.ksAndKPList[this.data.selectedKSIDIndex].kpNames[e.detail.value],
        })
    },

    bindExerciseTypePickerChange(e){
        this.setData({
            selectedExericseType:this.data.exerciseTypes[e.detail.value]
        })
    },

    bindExerciseTypePickerChange(e){
        this.setData({
            selectedExerciseType:this.data.exerciseTypes[e.detail.value]
        })
    },

    bindOptionCountPickerChange(e){
        var lastCount=this.data.selectedOptionCount
        this.setData({
            selectedOptionCount:this.data.choiceOptionCounts[e.detail.value],
        })
        console.log((lastCount))
        console.log(this.data.selectedOptionCount)
        for(var i=0;i<8;i++){
            if(i>=lastCount&&i<this.data.selectedOptionCount){
                this.data.choiceOptionContents[i]='',
                this.data.choiceOptionKeyCounts[i]=i+1
            }
        }
        for(var i=8;i>=this.data.selectedOptionCount;i--){
            this.data.choiceOptionContents.splice(i,1)
            this.data.choiceOptionKeyCounts.splice(i,1)
        }
        this.setData({
            choiceOptionContents:this.data.choiceOptionContents,
            choiceOptionKeyCounts:this.data.choiceOptionKeyCounts,
        })
        this.setData({
            selectedChoiceKey:this.data.choiceOptionKeyCounts[0]
        })
    },

    bindChoiceKeyPickerChange:function(e){
        this.setData({
            selectedChoiceKey:this.data.choiceOptionKeyCounts[e.detail.value]
        })
    },

    bindJudgmentKeyPickerChange:function(e){
        this.setData({
            selectedJudgmentKey:this.data.judgmentOptionKeys[e.detail.value]
        })
    },

    stemInput:function(e){
        this.setData({
          stemVal:e.detail.value
        })
    },

    choiceOptionContentsInput(e){
        this.data.choiceOptionContentsBuffer[e.currentTarget.dataset.num]=e.detail.value
        this.setData({
            choiceOptionContentsBuffer:this.data.choiceOptionContentsBuffer
        })
    },

    confirmAddExercise:function(){
        this.setData({
            addExerciseConfirm:true
        })
    },

    cancelAddExercise:function(){
        this.setData({
            addExerciseConfirm:false
        })
    },

    addExercise:function(){
        this.setData({
            addExerciseConfirm:false
        })
        var that=this
        console.log(that.data.selectedExerciseType)
        if(that.data.selectedExerciseType=="选择题"){
            wx.request({
                url: 'http://'+getApp().globalData.ip+':8080/addChoiceExercise',
                data:{
                    moduleID:that.data.selectedModuleID,
                    ksID:that.data.selectedKSID,
                    kpID:that.data.selectedKPID,
                    stem:that.data.stemVal,
                    eKey:that.data.selectedChoiceKey,
                    eOptions:JSON.stringify(that.data.choiceOptionContentsBuffer)
                },
                header: {
                    'content-type': 'application/json' // 默认值
                },
                success: function (res) { 
                   
                }
            })
        }
        else if(that.data.selectedExerciseType=="判断题"){
            wx.request({
                url: 'http://'+getApp().globalData.ip+':8080/addJudgmentExercise',
                data:{
                    moduleID:that.data.selectedModuleID,
                    ksID:that.data.selectedKSID,
                    kpID:that.data.selectedKPID,
                    stem:that.data.stemVal,
                    eKey:that.data.selectedChoiceKey
                },
                header: {
                    'content-type': 'application/json' // 默认值
                },
                success: function (res) { 
                    console.log(that.data.selectedChoiceKey)
                }
            })
        }
    }

})