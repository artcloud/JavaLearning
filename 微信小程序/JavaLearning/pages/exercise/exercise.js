// pages/exercise/exercise.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        timerStarted:false,
        start:0,
        endTime:0,
        wrap:false,
        student_ID:'',
        moduleID:'',
        exerciseNum:'',
        exerciseCount:'',
        eType:'',
        eKey:'',
        stem:'',
        radioList: [
            /*{
              optionName:'',
              optionContent:'',
              optionText:'',
              checked:false
            },*/
          ],
          blankList: [
            /*{
                blankNum:'',
            },*/
          ],
          blankInputList:[],
          showClearBtn: [],
          rowList:[
              /*
                  names:[],
              */
          ],
          blankValues:[],
          radioChoiceList:[],
          radioJudgmentList:[],
          exerciseDone:[],
          exerciseTimeUsed:[],
        exerciseNavigator:false,
        submitConfirm:false,
        allExercisesDone:false,
        canDoReachBottom:true,
        submitted:false
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        var that=this
        this.setData({
            student_ID:getApp().globalData.student_ID,
            moduleID:options.moduleID,
            exerciseNum:0,
            submitted:false
        })
        wx.request({
            url: 'http://'+getApp().globalData.ip+':8080/getExerciseCount',
            data:{
                student_ID:that.data.student_ID,
                moduleID:that.data.moduleID,
            },
            header: {
              'content-type': 'application/json' // 默认值
            },
            success: function (res) { 
                that.setData({
                    exerciseCount:parseInt(res.data),
                    rowList:[]
                })
                for(var i=0;i<that.data.exerciseCount;i++){
                    that.data.exerciseDone[i]=false

                    if(i%4==0){
                        var newarray2=[{
                            names:[i+1]
                        }]
                        that.setData({
                            rowList:that.data.rowList.concat(newarray2)
                        })
                    }
                    else{
                        that.data.rowList[Math.floor(i/4)].names=that.data.rowList[Math.floor(i/4)].names.concat(i+1)
                        that.setData({
                            rowList:that.data.rowList
                        })
                    }
                    that.setData({
                        exerciseDone:that.data.exerciseDone,
                    })
                    that.data.exerciseTimeUsed[i]=0
                    that.setData({
                        exerciseTimeUsed:that.data.exerciseTimeUsed
                    })
                }
            }
        })
        this.countDown()
        this.renewExercise()
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
        this.setData({
            submitted:true
        })
    },

    /**
     * 生命周期函数--监听页面卸载
     */
    onUnload: function () {
        this.setData({
            submitted:true
        })
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
        if(this.data.canDoReachBottom){
            this.setData({
                exerciseNavigator:true
            })
        }
    },

    /**
     * 用户点击右上角分享
     */
    onShareAppMessage: function () {

    },

    prevExercise:function(){

    },

    changeExerciseNum:function(e){
        this.exerciseNavigatorClose()
        this.data.exerciseTimeUsed[this.data.exerciseNum]+=(this.data.timeExerciseStart-this.data.endTime)/1000
        this.setData({
            exerciseTimeUsed:this.data.exerciseTimeUsed
        })
        var k=e.currentTarget.dataset.num
        if((k<this.data.exerciseNum&&this.data.exerciseNum>0)||(k>this.data.exerciseNum&&this.data.exerciseNum<this.data.exerciseCount-1)){
            this.setData({
                exerciseNum:k
            })
            this.renewExercise()
        }
    },

    renewExercise:function(){
        this.setData({
            timeExerciseStart:this.data.endTime
        })
        var that=this
        wx.request({
            url: 'http://'+getApp().globalData.ip+':8080/getExercise',
            data:{
                student_ID:that.data.student_ID,
                moduleID:that.data.moduleID,
                exerciseNum:that.data.exerciseNum
            },
            header: {
              'content-type': 'application/json' // 默认值
            },
            success: function (res) { 
                if(res.data[0][2]=="choice"||res.data[0][2]=="judgment"){
                    that.setData({
                        eKey:res.data[0][0],
                        stem:res.data[0][1],
                        eType:res.data[0][2],
                        radioList:[]
                    })
                    for(var i=0;i<res.data[1].length;i++){
                        var newarray=[]
                        if(that.data.eType=="choice"){
                            newarray=[{
                                optionName:res.data[1][i],
                                checked:that.data.radioChoiceList[that.data.exerciseNum]==res.data[1][i]?true:false,
                                optionContent:res.data[2][i],
                                optionText:res.data[1][i]+"."+res.data[2][i]
                            }]
                        }
                        else if(that.data.eType=="judgment"){
                            newarray=[{
                                optionName:res.data[1][i],
                                checked:that.data.radioJudgmentList[that.data.exerciseNum]==res.data[1][i]?true:false,
                                optionText:res.data[1][i]
                            }]
                        }
                        that.setData({
                            radioList:that.data.radioList.concat(newarray)
                        })
                    }
                }
                else if(res.data[0][2]=="blank"){
                    that.setData({
                        stem:res.data[0][1],
                        eType:res.data[0][2],
                        blankList:[],
                        blankValues:[],
                        blankCount:parseInt(res.data[0][3])
                    })
                    for(var i=0;i<parseInt(that.data.blankCount);i++){
                        var newarray=[{
                            blankNum:i
                        }]
                        that.setData({
                            blankList:that.data.blankList.concat(newarray)
                        })
                    }
                    if(that.data.blankInputList[that.data.exerciseNum]==null){
                        var newarray=[{
                            blanks:[]
                        }]
                        that.data.blankInputList[that.data.exerciseNum]=newarray
                        var arrayer=[]
                        for(var i=0;i<that.data.blankCount;i++){
                            arrayer=arrayer.concat('')
                        }
                        that.data.blankInputList[that.data.exerciseNum].blanks=arrayer
                        that.setData({
                            blankInputList:that.data.blankInputList
                        })
                    }
                    else{
                        that.data.blankValues=that.data.blankInputList[that.data.exerciseNum].blanks
                        that.setData({
                            blankValues:that.data.blankValues
                        })
                    }
                }
            }
        })
    },

    radioChange(e) {
        if(this.data.eType=="choice"){
            this.data.radioChoiceList[this.data.exerciseNum]=e.detail.value
        }
        else if(this.data.eType=="judgment"){
            this.data.radioJudgmentList[this.data.exerciseNum]=e.detail.value
        }
        this.data.exerciseDone[this.data.exerciseNum]=true
        this.setData({
            radioChoiceList:this.data.radioChoiceList,
            radioJudgmentList:this.data.radioJudgmentList,
            exerciseDone:this.data.exerciseDone
        })
    },

    onInput(e) {
        const { value } = e.detail;
        this.data.blankInputList[this.data.exerciseNum].blanks[parseInt(e.currentTarget.dataset.num)]=value
        this.data.blankValues[parseInt(e.currentTarget.dataset.num)]=value
        var flag=true
        for(var i=0;i<this.data.blankCount;i++){
            if(this.data.blankValues[i]==undefined||this.data.blankValues[i]==''){
                flag=false
                break
            }
        }
        this.data.exerciseDone[this.data.exerciseNum]=flag
        this.data.showClearBtn[e.currentTarget.dataset.num]=!!value.length
        this.setData({
            blankValues:this.data.blankValues,
            values:this.data.blankValues.values,
            blankInputList:this.data.blankInputList,
            exerciseDone:this.data.exerciseDone,
            showClearBtn: this.data.showClearBtn,
        });
      },

    onClear(e) {
        this.data.blankValues[e.currentTarget.dataset.num]=''
        this.data.showClearBtn[e.currentTarget.dataset.num]=false
        this.setData({
          blankValues:this.data.blankValues,
          showClearBtn: this.data.showClearBtn,
          isWaring: false,
        });
      },

    submitModuleExercisesConfirm:function(){
        this.exerciseNavigatorClose()
        this.setData({
            canDoReachBottom:false
        })
        var flag=true
        for(var i=0;i<this.data.exerciseCount;i++){
            if(!this.data.exerciseDone[i]){
                flag=false
                break
            }
        }
        this.data.allExercisesDone=flag
        this.setData({
            allExercisesDone:this.data.allExercisesDone,
            submitConfirm:true
        })
    },

    submitModuleExercises:function(){
        this.setData({
            submitted:true
        })
        var that=this
        var dataChoiceExercise=[]
        var dataJudgmentExercise=[]
        var dataBlankExercise=[]
        for(var i=0;i<that.data.exerciseCount;i++){
            dataChoiceExercise[i]=that.data.radioChoiceList[i]
            dataJudgmentExercise[i]=that.data.radioJudgmentList[i]
            if(that.data.blankInputList[i]!=null){
                dataBlankExercise[i]=that.data.blankInputList[i].blanks
            }
            else{
                var nw=[]
                dataBlankExercise[i]=[]
            }
        }
        this.data.exerciseTimeUsed[this.data.exerciseNum]+=(this.data.timeExerciseStart-this.data.endTime)/1000
        this.setData({
            exerciseTimeUsed:this.data.exerciseTimeUsed
        })
        console.log(dataBlankExercise[3])
        wx.request({
            url: 'http://'+getApp().globalData.ip+':8080/submitModuleExerciseSet',
            method:'POST',
            data:{
                student_ID:that.data.student_ID,
                moduleID:that.data.moduleID,
                choiceExerciseData:JSON.stringify(dataChoiceExercise),
                judgmentExerciseData:JSON.stringify(dataJudgmentExercise),
                blankExerciseData:JSON.stringify(dataBlankExercise),
                exerciseTimeUsed:JSON.stringify(that.data.exerciseTimeUsed),
                totalTimeUsed:JSON.stringify((15 * 60000-that.data.endTime)/1000)
              },
            header: {
              'content-type': 'application/x-www-form-urlencoded' // 默认值
            },
            success: function (res) {
                that.closeSubmitModuleExercisesConfirm(1)
                if(parseInt(res.data.score)>=90){
                    wx.request({
                        url: 'http://'+getApp().globalData.ip+':8080/setModuleFinished',
                        method:'POST',
                        data:{
                            student_ID:that.data.student_ID,
                            moduleID:that.data.moduleID,
                          },
                        header: {
                          'content-type': 'application/x-www-form-urlencoded' // 默认值
                        },
                        success: function (res) {
                        }
                    })
                }
                wx.redirectTo({
                    url: '../exerciseAfterSubmit/exerciseAfterSubmit?score='+res.data.score+"&timeSubmit="+res.data.timeSubmit+"&moduleID="+that.data.moduleID,
                })
                getApp().globalData.completedRecordRenew=true
            }
        })
    },

    exerciseNavigatorClose() {
        this.setData({
            exerciseNavigator:false
        });
        wx.pageScrollTo({
          scrollTop:0,
        })
    },

    gotoUndone(){
        var to=0
        if(!this.data.allExercisesDone){
            for(var i=0;i<this.data.exerciseCount;i++){
                if(!this.data.exerciseDone[i]){
                    to=i
                    break
                }
            }
            this.setData({
                exerciseNum:to
            })
            this.renewExercise()
        }
    },

    closeSubmitModuleExercisesConfirm(i) {
        if(i!=1){
            this.gotoUndone()
        }
        this.setData({
          submitConfirm:false,
          canDoReachBottom:true
        });
      },

    countDown() {
        var that = this
    
        var date = new Date(); //现在时间
        var m,s,ds
        if(!this.data.timerStarted){
            setTimeout(that.countDown, 100);
            that.setData({
                endTime:15 * 600 *100,
                timerStarted:true
            })
        }
        else{
            if(that.data.endTime>0){
                setTimeout(that.countDown, 100);
                that.setData({
                    endTime:that.data.endTime-100,
                })
            }
            else{
                if(this.data.submitted) return
              that.setData({
                endTime:0
              })
              that.submitModuleExercises()
              return
            }
        }
        m = Math.floor(that.data.endTime / 1000 / 60 % 60);
        s = Math.floor(that.data.endTime / 1000 % 60);
        ds=Math.floor(that.data.endTime % 1000 / 100);
        that.setData({
            countdown: m + "：" + s+": "+ds,
        })
    },
})