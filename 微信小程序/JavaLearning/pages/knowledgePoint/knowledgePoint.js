// pages/knowledgePoint/knowledgePoint.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        mode:'',
        moduleID:'',
        ksID:'',
        kpID:'',
        kpName:'',
        kpContent:'',
        minksID:'',
        maxksID:'',
        minkpID:'',
        maxkpID:'',
        wrap:false,
        hasprev:false,
        hasnext:false,
        kpIfLearnt:'',
        finishLearningKPToast:false,
        hideFinishLearningKPToast:false
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        var that=this
        this.setData({ 
            student_ID:getApp().globalData.student_ID,
            mode:options.mode,
            moduleID:options.moduleID,
            ksID:options.ksID,
            kpID:options.kpID
        })
        wx.request({
            url: 'http://'+getApp().globalData.ip+':8080/getMinMaxkp',
            data:{
                moduleID:that.data.moduleID,
              },
            header: {
              'content-type': 'application/json' // 默认值
            },
            success: function (res) {
                that.setData({
                    minksID:res.data[0],
                    minkpID:res.data[1],
                    maxksID:res.data[2],
                    maxkpID:res.data[3]
                })
                that.changePrevNext()
            }
        })
        this.checkKPIfLearnt()
        wx.request({
            url: 'http://'+getApp().globalData.ip+':8080/getKnowledgePoint',
            data:{
                moduleID:that.data.moduleID,
                ksID:that.data.ksID,
                kpID:that.data.kpID
              },
            header: {
              'content-type': 'application/json' // 默认值
            },
            success: function (res) {
                that.setData({
                    kpName:res.data[0],
                })
                if(res.data[1]!=null){
                    that.setData({
                        kpContent:res.data[1].replace(/\\n/g,"\n"),
                    })
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
    onShow() {
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
        console.log("chudu")
    },

    /**
     * 用户点击右上角分享
     */
    onShareAppMessage: function () {

    },

    prevKP:function(){
        var that=this
        if(that.data.ksID==that.data.minksID&&that.data.kpID==that.data.minkpID) return
        wx.request({
            url: 'http://'+getApp().globalData.ip+':8080/getPrevkp',
            data:{
                moduleID:that.data.moduleID,
                ksID:that.data.ksID,
                kpID:that.data.kpID
              },
            header: {
              'content-type': 'application/json' // 默认值
            },
            success: function (res) {
                that.setData({
                    ksID:res.data[1],
                    kpID:res.data[2],
                    kpName:res.data[3],
                })
                if(res.data[4]!=null){
                    that.setData({
                        kpContent:res.data[4].replace(/\\n/g,"\n"),
                    })
                }
                else{
                    that.setData({
                        kpContent:''
                    })
                }
                that.checkKPIfLearnt()
                that.changePrevNext()
            }
        })
    },

    nextKP:function(){
        var that=this
        if(that.data.ksID==that.data.maxksID&&that.data.kpID==that.data.maxkpID) return
        wx.request({
            url: 'http://'+getApp().globalData.ip+':8080/getNextkp',
            data:{
                moduleID:that.data.moduleID,
                ksID:that.data.ksID,
                kpID:that.data.kpID
              },
            header: {
              'content-type': 'application/json' // 默认值
            },
            success: function (res) {
                that.setData({
                    ksID:res.data[1],
                    kpID:res.data[2],
                    kpName:res.data[3],
                })
                if(res.data[4]!=null){
                    that.setData({
                        kpContent:res.data[4].replace(/\\n/g,"\n"),
                    })
                }
                else{
                    that.setData({
                        kpContent:''
                    })
                }
                that.checkKPIfLearnt()
                that.changePrevNext()
            }
        })
    },

    checkKPIfLearnt:function(){
        var that=this
        wx.request({
            url: 'http://'+getApp().globalData.ip+':8080/getKPIfLearnt',
            data:{
                student_ID:that.data.student_ID,
                moduleID:that.data.moduleID,
                ksID:that.data.ksID,
                kpID:that.data.kpID
            },
            header: {
              'content-type': 'application/json' // 默认值
            },
            success: function (res) {
                that.setData({
                    kpIfLearnt:res.data
                })
            }
        })
    },

    finishLearningKP:function(){
        var that=this
        wx.request({
            url: 'http://'+getApp().globalData.ip+':8080/setFinishLearningKP',
            data:{
                student_ID:that.data.student_ID,
                moduleID:that.data.moduleID,
                ksID:that.data.ksID,
                kpID:that.data.kpID
              },
            header: {
              'content-type': 'application/json' // 默认值
            },
            success: function (res) {
                that.openFinishLearningKPToast()
                that.setData({
                    kpIfLearnt:true
                })
            }
        })
    },

    changePrevNext:function(){
        if(this.data.ksID==this.data.minksID&&this.data.kpID==this.data.minkpID){
            this.setData({
                hasprev:false
            })
        }
        else{
            this.setData({
                hasprev:true
            })
        }
        if(this.data.ksID==this.data.maxksID&&this.data.kpID==this.data.maxkpID){
            this.setData({
                hasnext:false
            })
        }
        else{
            this.setData({
                hasnext:true
            })
        }
    },

    openFinishLearningKPToast() {
        this.setData({
          finishLearningKPToast: true,
        });
        setTimeout(() => {
          this.setData({
            hideFinishLearningKPToast: true,
          });
          setTimeout(() => {
            this.setData({
                finishLearningKPToast: false,
                hideFinishLearningKPToast: false,
            });
          }, 100);
        }, 800);
    },
    
    anotherOne:function(){
        var that=this
        wx.request({
            url: 'http://'+getApp().globalData.ip+':8080/getRecommendKP',
            data:({
                student_ID:that.data.student_ID
            }),
            header: {
                'content-type': 'application/json'
            },
            success: function (res) {
                that.setData({
                    moduleID:res.data.moduleID,
                    ksID:res.data.ksID,
                    kpID:res.data.kpID
                })
            },
        })
        wx.request({
            url: 'http://'+getApp().globalData.ip+':8080/getKnowledgePoint',
            data:{
                moduleID:that.data.moduleID,
                ksID:that.data.ksID,
                kpID:that.data.kpID
              },
            header: {
              'content-type': 'application/json' // 默认值
            },
            success: function (res) {
                that.setData({
                    kpName:res.data[0],
                })
                if(res.data[1]!=null){
                    that.setData({
                        kpContent:res.data[1].replace(/\\n/g,"\n"),
                    })
                }
            }
        })
    }
})