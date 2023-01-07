// pages/register/register.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        student_ID:'',
        password:'',
        passwordConfirm:'',
        registerFailToast:'',
        hideRegisterFailToast:false,
        items:[
          {name: 'USA', value: '美国'},
          {name: 'CHN', value: '中国'},
         {name: 'BRA', value: '巴西'},
         {name: 'JPN', value: '日本'},
         {name: 'ENG', value: '英国'},
         {name: 'TUR', value: '法国'},
      ]
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
    register: function () {
        if(this.data.student_ID==""){
          this.openRegisterFailToast("账号不能为空")
          return
        }
        if(this.data.password==""){
          this.openRegisterFailToast("密码不能为空")
          return
        }
        if(this.data.password!=this.data.passwordConfirm){
          this.openRegisterFailToast("密码不一致")
          return
        }
        var that = this;
        wx.request({
          url: 'http://'+getApp().globalData.ip+':8080/register',
          data:{
            student_ID:this.data.student_ID,
            student_password:this.data.password
          },
          header: {
            'content-type': 'application/json' // 默认值
          },
          success: function (res) {
            console.log(res.data)//打印到控制台
            var data = res.data;
            
            if (data == "duplicated") {
              that.openRegisterFailToast("用户名已存在")
            } else {
              that.openRegisterSuccessToast("注册成功")
            }
          }
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

    passwordConfirmInput:function(e){
        this.setData({
          passwordConfirm:e.detail.value
        })
    },

    radioChange:function(e){
      console.log(e.detail.value)
    },

    openRegisterSuccessToast() {
      this.setData({
        registerSuccessToast: true,
      });
      setTimeout(() => {
        this.setData({
          hideRegisterSuccessToast: true,
        });
        setTimeout(() => {
          this.setData({
              registerSuccessToast: false,
              hideRegisterSuccessToast: false,
          });
          wx.navigateBack({
            delta:1
          })
        }, 100);
      }, 800);
    },

    openRegisterFailToast(txt) {
      this.setData({
        registerFailToast: true,
        failureMessage:txt
      });
      setTimeout(() => {
        this.setData({
          hideRegisterFailToast: true,
        });
        setTimeout(() => {
          this.setData({
              registerFailToast: false,
              hideRegisterFailToast: false,
          });
        }, 100);
      }, 800);
    },
})