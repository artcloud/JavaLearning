// pages/exerciseAfterSubmit/exerciseAfterSubmit.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        score:0,
        submitTime:'',
        moduleID:'',
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        this.setData({
            score:options.score,
            timeSubmit:options.timeSubmit,
            moduleID:options.moduleID
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

    gotoLearning:function(){
        if(this.data.score>=90){
            this.setData({
                moduleID:String(String(parseInt(this.data.moduleID)+1))
            })
        }
        wx.redirectTo({
          url: "../module/module?moduleID="+this.data.moduleID
        })
    }
})