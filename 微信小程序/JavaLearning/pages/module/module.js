// pages/module/module.js
Page({
    /**
     * 页面的初始数据
     */
    data: {
        moduleID:'',
        student_ID:'',
        po:[],
        locator:[],
        list: [
            /*{
              id: 'form',
              name: '表单',
              open: false,
              kpids:[0,1,2],
              kpnames: ['button', 'input', 'form', 'list', 'slideview', 'slider', 'uploader'],
              kpIDsLearnt:[0,1,2],
              ksIfLearnt:false,
            }*/
          ],
        exerciseLockedWarning:false
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        this.setData({ 
            student_ID:getApp().globalData.student_ID,
            moduleID:options.moduleID,
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
        console.log(getCurrentPages())
        this.setData({
          list:[]
        })
        var that=this
        wx.request({
          url: 'http://'+getApp().globalData.ip+':8080/getKnowledgeSetsAndPoints',
          data:{
              student_ID:that.data.student_ID,
              moduleID:that.data.moduleID
          },
          header: {
            'content-type': 'application/json' // 默认值
          },
          success: function (res) { 
              for(var i=0;i<res.data.length;i++){
                  var c=res.data[i].ksLearnt=="true"?true:false
                  var newarray=[{
                      id:i+1,
                      name:res.data[i].ksName[0],
                      open:false,
                      kpids:res.data[i].kpIDs,
                      kpnames:res.data[i].kpNames,
                      kpIDsLearnt:res.data[i].learntKPIDs,
                      ksIfLearnt:c
                  }]
                  that.setData({
                      list:that.data.list.concat(newarray)
                  })
              }
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

    kindToggle(e) {
        const { id } = e.currentTarget; const { list } = this.data;
        for (let i = 0, len = list.length; i < len; ++i) {
          if (list[i].id == id) {
            list[i].open = !list[i].open;
          } else {
            list[i].open = false;
          }
        }
        this.setData({
          list,
        });
    },

    checkKPsIfLearnt:function(){
      var that=this
      wx.request({
          url: 'http://'+getApp().globalData.ip+':8080/getKPsIfLearnt',
          data:{
              student_ID:that.data.student_ID,
              moduleID:that.data.moduleID
            },
          header: {
            'content-type': 'application/json' // 默认值
          },
          success: function (res) {
            if(res.data==true){
              that.gotoExercise()
            }
            else{
              that.setData({
                exerciseLockedWarning: true,
              });
            }
          }
      })
    },
    gotoExercise(){
      wx.redirectTo({
        url: "../exerciseAccess/exerciseAccess?student_ID="+this.data.student_ID+"&moduleID="+this.data.moduleID
      })
    },
    closeExerciseLockedWarning() {
      this.setData({
        exerciseLockedWarning:false
      });
    },
})