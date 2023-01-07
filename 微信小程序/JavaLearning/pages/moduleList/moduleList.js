// pages/moduleList/moduleList.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        student_ID:'',
        learntModuleNameList:'',
        UnfinishedModuleNameList:'',
        lockedModuleNameList:'',
        finishedModuleIDList:'',
        UnfinishedmoduleIDList:'',
        lockedmoduleIDList:'',
        moduleLockedWarning:false,
        warnName:'',
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
        wx.request({
            url: 'http://'+getApp().globalData.ip+':8080/getFinishedModules',
            data:{
                student_ID:that.data.student_ID
              },
            header: {
              'content-type': 'application/json' // 默认值
            },
            success: function (res) {
                that.setData({
                    finishedModuleIDList:res.data[0],
                    finishedModuleNameList:res.data[1]
                })          
            }
        })
        wx.request({
            url:'http://'+getApp().globalData.ip+':8080/getUnfinishedModules',
            data:{
                student_ID:that.data.student_ID
              },
            header: {
              'content-type': 'application/json' // 默认值
            },
            success: function (res) {
                that.setData({
                    unfinishedModuleIDList:res.data[0],
                    unfinishedModuleNameList:res.data[1]
                })          
            }
        })
        wx.request({
            url: 'http://'+getApp().globalData.ip+':8080/getLockedModules',
            data:{
                student_ID:that.data.student_ID
              },
            header: {
              'content-type': 'application/json' // 默认值
            },
            success: function (res) {
                that.setData({
                    lockedmoduleIDList:res.data[0],
                    lockedModuleNameList:res.data[1]
                })          
            }
        })
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

    goback:function(){
        wx.switchTab({
            url:"../student_homepage/student_homepage"
        })
    },


    lockedWarning(e) {
        var that=this
        wx.request({
            url: 'http://'+getApp().globalData.ip+':8080/getDependencyNames',
            data:{
                moduleID:e.currentTarget.dataset.num
            },
            header: {
              'content-type': 'application/json' // 默认值
            },
            success: function (res) {
                var wn=''
                for(var i=0;i<res.data.length;i++){
                    wn+=res.data[i]
                    if(i!=res.data.length-1) wn+='、'
                }
                that.setData({
                    warnName:wn
                })          
            }
        })
        this.setData({
            moduleLockedWarning: true,
          });
      },
      closeModuleLockedWarning() {
        this.setData({
          moduleLockedWarning:false
        });
      },
})