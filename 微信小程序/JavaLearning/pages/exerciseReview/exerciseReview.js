// pages/exerciseReview/exerciseReview.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        student_ID:'',
        timeSubmit:'',
        choiceExerciseList: [
            /*{
              optionName:'',
              optionContent:'',
              optionText:'',
            },*/
          ],
          judgmentExerciseList: [
            /*{
              optionText:'',
            },*/
          ],
          blankExerciseList:[],
          esType:'',
          moduleName:'',
          totalTimeUsed:'',
          score:'',
          correctRate:'',
          exerciseNum:1,
          showRelatedKPsDialog: false,
          showshowKPDialog:false,
          kpList:[],
          moduleID:'',
          ksID:'',
          kpID:'',
          kpContent:''
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        var that=this
        this.setData({
            student_ID:getApp().globalData.student_ID,
            timeSubmit:options.timeSubmit
        })
        wx.request({
            url: 'http://'+getApp().globalData.ip+':8080/getReviewExerciseSetData',
            data:{
                student_ID:that.data.student_ID,
                timeSubmit:that.data.timeSubmit
              },
            header: {
              'content-type': 'application/json' // 默认值
            },
            success: function (res) {
                that.setData({
                    esType:res.data.esType,
                    totalTimeUsed:res.data.totalTimeUsed,
                    score:res.data.score,
                    correctRate:res.data.correctRate,
                    moduleName:res.data.moduleName
                })
            }
        })
        wx.request({
            url: 'http://'+getApp().globalData.ip+':8080/getReviewExerciseData',
            data:{
                student_ID:that.data.student_ID,
                timeSubmit:that.data.timeSubmit
              },
            header: {
              'content-type': 'application/json' // 默认值
            },
            success: function (res) {
                for(var i=0;i<res.data.length;i++){
                    if(res.data[i][0][1]=="choice"){
                        var arrayUnitOptionID=[]
                        for(var j=0;j<res.data[i][1].length;j++){
                            if(res.data[i][1][j]!=null){
                                arrayUnitOptionID=arrayUnitOptionID.concat(res.data[i][1][j])
                            }
                        }
                        var arrayUnitOptionContent=[]
                        for(var j=0;j<res.data[i][1].length;j++){
                            if(res.data[i][2][j]!=null){
                                arrayUnitOptionContent=arrayUnitOptionContent.concat(res.data[i][2][j])
                            }
                        }
                        var newarray=[{
                            id:that.data.exerciseNum,
                            optionID:arrayUnitOptionID,
                            optionContent:arrayUnitOptionContent,
                            eID:res.data[i][0][0],
                            stem:res.data[i][0][3],
                            key:res.data[i][0][4],
                            timeUsed:res.data[i][0][5],
                            answer:res.data[i][0][6],
                            score:res.data[i][0][7]
                        }]
                        that.setData({
                            exerciseNum:that.data.exerciseNum+1,
                            choiceExerciseList:that.data.choiceExerciseList.concat(newarray)
                        })
                    }
                    else if(res.data[i][0][1]=="judgment"){
                        var newarray=[{
                          id:that.data.exerciseNum,
                          eID:res.data[i][0][0],
                          stem:res.data[i][0][3],
                          key:res.data[i][0][4],
                          timeUsed:res.data[i][0][5],
                          answer:res.data[i][0][6],
                          score:res.data[i][0][7]
                        }]
                        that.setData({
                            exerciseNum:that.data.exerciseNum+1,
                            judgmentExerciseList:that.data.judgmentExerciseList.concat(newarray)
                        })
                    }
                    else if(res.data[i][0][1]=="blank"){
                      var arrayBlankKey=[]
                        for(var j=0;j<res.data[i][1].length;j++){
                            if(res.data[i][1][j]!=null){
                                arrayBlankKey=arrayBlankKey.concat(res.data[i][1][j])
                            }
                        }
                        var arrayBlankAnswer=[]
                        for(var j=0;j<res.data[i][1].length;j++){
                            if(res.data[i][2][j]!=null){
                                arrayBlankAnswer=arrayBlankAnswer.concat(res.data[i][2][j])
                            }
                        }
                        var newarray=[{
                          id:that.data.exerciseNum,
                          eID:res.data[i][0][0],
                          stem:res.data[i][0][3],
                          key:res.data[i][0][4],
                          timeUsed:res.data[i][0][5],
                          blankKey:arrayBlankKey,
                          blankAnswer:arrayBlankAnswer,
                          score:res.data[i][0][7]
                        }]
                        that.setData({
                          exerciseNum:that.data.exerciseNum+1,
                          blankExerciseList:that.data.blankExerciseList.concat(newarray)
                        })
                    }
                }
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

    openRelatedKPsDialog(e) {
        this.setData({
          kpList:[]
        })
        var that=this
        wx.request({
            url: 'http://'+getApp().globalData.ip+':8080/getRelatedKPs',
            data:{
                eID:e.currentTarget.dataset.eid
            },
            header: {
              'content-type': 'application/json' // 默认值
            },
            success: function (res) {
                for(var i=0;i<res.data.moduleIDs.length;i++){
                  var newarray=[{
                      moduleID:res.data.moduleIDs[i],
                      ksID:res.data.ksIDs[i],
                      kpID:res.data.kpIDs[i],
                      kpName:res.data.kpNames[i]
                  }]
                  that.setData({
                      kpList:that.data.kpList.concat(newarray),
                      showRelatedKPsDialog: true,
                  })
                }
            }
        })
    },

    closeRelatedKPsDialog() {
        this.setData({
          showRelatedKPsDialog: false,
        });
      },

     openKPDialog(e) {
        var that=this
          wx.request({
            url: 'http://'+getApp().globalData.ip+':8080/getKnowledgePoint',
            data:{
                moduleID:e.currentTarget.dataset.moduleid,
                ksID:e.currentTarget.dataset.ksid,
                kpID:e.currentTarget.dataset.kpid
            },
            header: {
              'content-type': 'application/json' // 默认值
            },
            success: function (res) {
              that.setData({
                kpName:res.data[0],
                kpContent:res.data[1]!=null?res.data[1].replace(/\\n/g,"\n"):"",
                showKPDialog: true
              })
            }
        })
      },

     closeKPDialog() {
        this.setData({
          showKPDialog: false
        });
      },
})