// pages/login/login.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        student_ID:'',
        password:'',
        loginSuccessToast:false,
        hideLoginSuccessToast:false
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {

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

    goto_index: function(){
        wx.switchTab({
            url: "../index/index"
        })
    },

    goto_register: function(){
        wx.redirectTo({
            url: "../admin_homepage/admin_homepage"
        })
    },

    create_login: function(){
      if(this.data.student_ID==""){
        this.openLoginFailToast("账号不能为空")
        return
      }
      if(this.data.password==""){
        this.openLoginFailToast("密码不能为空")
        return
      }
        var that = this;
        wx.request({
          url: 'http://'+getApp().globalData.ip+':8080/login',
          data:({
            ID:that.data.student_ID,
            student_password:that.data.password
          }),
          header: {
            'content-type': 'application/json'
          },
          success: function (res) {
            var list = res.data;
            
            if (list == "fail") {
                that.openLoginFailToast("登录失败")
            }
            else if(list=="success") {
                that.openLoginSuccessToast()
                getApp().globalData.student_ID=that.data.student_ID
                setTimeout(function(){
                  wx.switchTab({
                    url: '../student_homepage/student_homepage',
                  })
                },1000);
            }
            else if(list=="adminsuccess"){
              that.openLoginSuccessToast()
              getApp().globalData.student_ID=that.data.student_ID
              setTimeout(function(){
                wx.redirectTo({
                  url: '../admin_homepage/admin_homepage',
                })
              },1000);
            }
          },
        })
    },

    student_IDInput:function(e){
        this.setData({
          student_ID:e.detail.value
        })
    },
    passwordInput:function(e){
        this.setData({
          password:e.detail.value
        })
    },

    openLoginSuccessToast() {
      this.setData({
        loginSuccessToast: true,
      });
      setTimeout(() => {
        this.setData({
          hideLoginSuccessToast: true,
        });
        setTimeout(() => {
          this.setData({
              loginSuccessToast: false,
              hideLoginSuccessToast: false,
          });
        }, 100);
      }, 800);
    },

    openLoginFailToast(txt) {
      this.setData({
        loginFailToast: true,
        failureMessage:txt
      });
      setTimeout(() => {
        this.setData({
          hideLoginFailToast: true,
        });
        setTimeout(() => {
          this.setData({
              loginFailToast: false,
              hideLoginFailToast: false,
          });
        }, 100);
      }, 800);
    },
})