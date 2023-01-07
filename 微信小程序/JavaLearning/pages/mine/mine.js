// pages/mine/mine.js
let radCtx = wx.createCanvasContext("radarCanvas");//雷达图
let radar = {
    mCenter: 180, //中心点
    hCenter: 120, //中心点
    mAngle: Math.PI * 2 / 3,
    mRadius: 90, //半径(减去的值用于给绘制的文本留空间)
    radarData: [[], [], []],
}

Page({

    /**
     * 页面的初始数据
     */
    data: {
        student_ID:'',
        countFinishedModule:0,
        countLearntKP:0,
        countExerciseSetCompleted:0,
        averageScore:0,
        averageCorrectRate:0,
        averageTimeUsed:'',
        averageTimeUsedFloat:0
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
            url: 'http://'+getApp().globalData.ip+':8080/getLearningData',
            data:{
                student_ID:that.data.student_ID
            },
            header: {
                'content-type': 'application/json' // 默认值
            },
            success: function (res) { 
               that.setData({
                    countFinishedModule:res.data.countFinishedModule,
                    countLearntKP:res.data.countLearntKP
               })
            }
        })
        wx.request({
            url: 'http://'+getApp().globalData.ip+':8080/getCompletedExerciseData',
            data:{
                student_ID:that.data.student_ID
            },
            header: {
                'content-type': 'application/json' // 默认值
            },
            success: function (res) { 
               that.setData({
                    countExerciseSetCompleted:res.data.countExerciseSetCompleted,
                   averageScore:res.data.averageScore,
                   averageCorrectRate:res.data.averageCorrectRate,
                   averageTimeUsed:res.data.averageTimeUsed,
                   averageTimeUsedFloat:res.data.averageTimeUsedFloat
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

    logout:function(){
        wx.request({
            url: 'http://'+getApp().globalData.ip+':8080/logOut',
            data:{
                student_ID:this.data.student_ID
            },
            header: {
                'content-type': 'application/json' // 默认值
            },
            success: function (res) { 
                getApp().globalData.student_ID=''
                wx.reLaunch({
                    url:"../login/login"
                })
            }
        })
    },

    openRadarChart(e) {
        this.setData({
            showRadarChart: true
        })
        console.log(this.data.averageScore)
        radar.radarData[0][0]="平均分"
        radar.radarData[1][0]="正确率"
        radar.radarData[2][0]="做题速度"
        radar.radarData[0][1]=this.data.averageScore
        radar.radarData[1][1]=this.data.averageCorrectRate.substr(0,this.data.averageCorrectRate.length-1)
        radar.radarData[2][1]=100-this.data.averageTimeUsedFloat/(15*60*10)
        this.drawRadar()
      },

     closeRadarChart() {
        this.setData({
          showRadarChart: false
        });
      },

      drawRadar() {
        const radarData = radar.radarData;
        this.drawEdge() //画六边形
        this.drawLinePoint()
        this.drawRegion(radarData, 'rgba(51, 136, 255, 0.4)') //设置数据
        this.drawTextCans(radarData)//设置文本数据
        this.drawCircle(radarData, '#3388FF')//设置节点
        radCtx.draw()//开始绘制
      },
      // 绘制6条边
      drawEdge() {  
        radCtx.setStrokeStyle("#eee");
        for (let i = 0; i < 3; i++) {
          //计算半径
          radCtx.beginPath();
          let rdius = radar.mRadius / 3 * (i + 1) - 0;
          //画6条线段
          for (let j = 0; j < 3; j++) {
            //坐标
            let x = radar.mCenter + rdius * Math.sin(radar.mAngle * j);
            let y = radar.hCenter + rdius * Math.cos(radar.mAngle * j);
            radCtx.lineTo(x, y);
          }
          radCtx.closePath()
          radCtx.stroke()
        }
      },
      // 绘制连接点
      drawLinePoint() {
        for (let k = 0; k < 3; k++) {
          let x = radar.mCenter + radar.mRadius * Math.sin(radar.mAngle * k);
          let y = radar.hCenter + radar.mRadius * Math.cos(radar.mAngle * k);
          radCtx.moveTo(radar.mCenter, radar.hCenter);
          radCtx.lineTo(x, y);
        }
        radCtx.stroke();
      },
      //绘制数据区域(数据和填充颜色)
      drawRegion(mData, color) {
        radCtx.beginPath();
        for (let m = 0; m < 3; m++) {
          let x = radar.mCenter + radar.mRadius * Math.sin(radar.mAngle * m) * mData[m][1] / 100;
          let y = radar.hCenter + radar.mRadius * Math.cos(radar.mAngle * m) * mData[m][1] / 100;
          radCtx.lineTo(x, y);
        }
        radCtx.closePath();
        radCtx.setFillStyle(color)
        radCtx.fill();
      },
      //绘制文字
      drawTextCans(mData) {
        radCtx.setFillStyle("#555")
        radCtx.setFontSize(12) //设置字体
        for (let n = 0; n < 3; n++) {
          let x = radar.mCenter + radar.mRadius * Math.sin(radar.mAngle * n);
          let y = radar.hCenter + radar.mRadius * Math.cos(radar.mAngle * n);
          //通过不同的位置，调整文本的显示位置
          if (n == 0) {
            radCtx.fillText(mData[0][0], x - 23, y+30);
          } else if (n == 1) {
            radCtx.fillText(mData[1][0], x + 30, y );
          } else if (n == 2) {
            radCtx.fillText(mData[2][0], x-70, y );
          } else if (n == 3) {
            radCtx.fillText(mData[3][0], x - 23, y - 10);
          } else if (n == 4) {
            radCtx.fillText(mData[4][0], x - 55, y + 5);
          } else if (n == 5) {
            radCtx.fillText(mData[5][0], x - 55, y + 5);
          }
        }
      },
      //画点
      drawCircle(mData, color) {
        const r = 5; //设置节点小圆点的半径
        for (let i = 0; i < 3; i++) {
          let x = radar.mCenter + radar.mRadius * Math.sin(radar.mAngle * i) * mData[i][1] / 100;
          let y = radar.hCenter + radar.mRadius * Math.cos(radar.mAngle * i) * mData[i][1] / 100;
          radCtx.beginPath();
          radCtx.arc(x, y, r, 0, Math.PI * 2);
          radCtx.fillStyle = color;
          radCtx.fill();
        }
      }
})