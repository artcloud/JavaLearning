// pages/exerciseAccess/exerciseAccess.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        student_ID:'',
        moduleID:'',
        moduleName:''
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        var that=this
        this.setData({
            student_ID:options.student_ID,
            moduleID:options.moduleID,
        })
        wx.request({
            url: 'http://'+getApp().globalData.ip+':8080/getModuleName',
            data:{
                moduleID:that.data.moduleID
            },
            header: {
              'content-type': 'application/json' // 默认值
            },
            success: function (res) { 
                that.setData({
                    moduleName:res.data
                })
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

    generateExercises:function(){
        var that=this
        wx.request({
            url: 'http://'+getApp().globalData.ip+':8080/generateExercises',
            data:{
                student_ID:that.data.student_ID,
                moduleID:that.data.moduleID
                
            },
            header: {
              'content-type': 'application/json' // 默认值
            },
            success: function (res) {
                wx.redirectTo({
                    url:"../exercise/exercise?moduleID="+that.data.moduleID
                })
            }
        })
    }
})