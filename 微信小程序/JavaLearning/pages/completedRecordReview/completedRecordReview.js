// pages/completedRecordReview/completedRecordReview.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        completedExerciseSetList:[],
        searchInputShowed:false,
        searchInputVal:''
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        this.setData({
            student_ID:getApp().globalData.student_ID
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
        var that=this
        if(getApp().globalData.completedRecordRenew){
            getApp().globalData.completedRecordRenew=false
            that.setData({
                searchInputShowed:false,
                searchInputVal:''
            })
            wx.request({
                url: 'http://'+getApp().globalData.ip+':8080/getCompletedExerciseSets',
                data:{
                    student_ID:that.data.student_ID
                },
                header: {
                  'content-type': 'application/json' // 默认值
                },
                success: function (res) {
                    that.setData({
                        completedExerciseSetList:[]
                    })
                    var displayLength=res.data.length>30?30:res.data.length
                    for(var i=0;i<displayLength;i++){
                        var newarray=[{
                            esType:res.data[i].esType,
                            moduleID:res.data[i].moduleID,
                            moduleName:res.data[i].moduleName,
                            timeSubmit:res.data[i].timeSubmit,
                            timeSubmitToDate:res.data[i].timeSubmit.substring(0,10),
                            totalTimeUsed:res.data[i].totalTimeUsed,
                            score:res.data[i].score,
                            correctRate:res.data[i].correctRate
                        }]
                        that.setData({
                            completedExerciseSetList:that.data.completedExerciseSetList.concat(newarray)
                        })
                    }
                }
            })
        }
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

    showSearchInput() {
        this.setData({
          searchInputShowed: true,
        });
    },

    hideSearchInput() {
        this.setData({
          searchInputVal: '',
          searchInputShowed: false,
        });
    },

    clearSearchInput() {
        this.setData({
            searchInputVal: '',
        });
    },
    searchInputTyping(e) {
        this.setData({
          searchInputVal: e.detail.value,
        });
        console.log(this.data.searchInputVal)
    },
})