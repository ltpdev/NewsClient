package com.gdcp.newsclient.bean;

import java.util.List;

/**
 * Created by asus- on 2017/8/26.
 */

public class Weather {


    /**
     * realtime : {"wind":{"windspeed":null,"direct":"西南风","power":"2级","offset":null},"time":"18:00:00","weather":{"humidity":"90","img":"3","info":"阵雨","temperature":"26"},"dataUptime":"1503743106","date":"2017-08-26","city_code":"101281201","city_name":"河源","week":"6","moon":"七月初五"}
     * life : {"date":"2017-8-26","info":{"kongtiao":["部分时间开启","您将感到些燥热，建议您在适当的时候开启制冷空调来降低温度，以免中暑。"],"yundong":["较不宜","有降水，推荐您在室内进行低强度运动；若坚持户外运动，须注意携带雨具。"],"ziwaixian":["中等","属中等强度紫外线辐射天气，外出时建议涂擦SPF高于15、PA+的防晒护肤品，戴帽子、太阳镜。"],"ganmao":["少发","各项气象条件适宜，发生感冒机率较低。但请避免长期处于空调房间中，以防感冒。"],"xiche":["不宜","不宜洗车，未来24小时内有雨，如果在此期间洗车，雨水和路上的泥水可能会再次弄脏您的爱车。"],"wuran":null,"chuanyi":["炎热","天气炎热，建议着短衫、短裙、短裤、薄型T恤衫等清凉夏季服装。"]}}
     * weather : [{"date":"2017-08-26","week":"六","nongli":"七月初五","info":{"dawn":null,"day":["4","雷阵雨","36","","微风","06:02","出门记得带伞，行走驾驶做好防滑准备"],"night":["3","阵雨","26","东北风","3-4 级","18:45","出门记得带伞，行走驾驶做好防滑准备"]}},{"date":"2017-08-27","week":"日","nongli":"七月初六","info":{"dawn":["3","阵雨","26","东北风","3-4 级","18:45"],"day":["9","大雨","29","东风","5-6 级","06:02","出门记得带伞，行走驾驶做好防滑准备"],"night":["8","中雨","25","东风","3-4 级","18:44","出门记得带伞，行走驾驶做好防滑准备"]}},{"date":"2017-08-28","week":"一","nongli":"七月初七","info":{"dawn":["8","中雨","25","东风","3-4 级","18:44"],"day":["3","阵雨","33","东南风","3-4 级","06:02"],"night":["1","多云","24","东北风","3-4 级","18:44"]}},{"date":"2017-08-29","week":"二","nongli":"七月初八","info":{"dawn":["1","多云","24","东北风","3-4 级","18:44"],"day":["4","雷阵雨","31","南风","3-4 级","06:03"],"night":["1","多云","24","","微风","18:43"]}},{"date":"2017-08-30","week":"三","nongli":"七月初九","info":{"dawn":["1","多云","24","无持续风向","微风","18:43"],"day":["4","雷阵雨","34","","微风","06:03"],"night":["3","阵雨","25","","微风","18:42"]}},{"date":"2017-08-31","week":"四","nongli":"七月初十 ","info":{"dawn":null,"day":["4","雷阵雨","35","东北风","微风","07:30"],"night":["1","多云","26","东北风","微风","19:30"]}},{"date":"2017-09-01","week":"五","nongli":"七月十一","info":{"dawn":null,"day":["4","雷阵雨","33","东北风","微风","07:30"],"night":["3","阵雨","26","东北风","微风","19:30"]}}]
     * pm25 : {"key":"Heyuan","show_desc":"0","pm25":{"curPm":"41","pm25":"11","pm10":"41","level":"1","quality":"优","des":"空气很好，可以外出活动"},"dateTime":"2017年08月26日18时","cityName":"河源"}
     * isForeign : 0
     */

    private ResultBean result;
    /**
     * result : {"realtime":{"wind":{"windspeed":null,"direct":"西南风","power":"2级","offset":null},"time":"18:00:00","weather":{"humidity":"90","img":"3","info":"阵雨","temperature":"26"},"dataUptime":"1503743106","date":"2017-08-26","city_code":"101281201","city_name":"河源","week":"6","moon":"七月初五"},"life":{"date":"2017-8-26","info":{"kongtiao":["部分时间开启","您将感到些燥热，建议您在适当的时候开启制冷空调来降低温度，以免中暑。"],"yundong":["较不宜","有降水，推荐您在室内进行低强度运动；若坚持户外运动，须注意携带雨具。"],"ziwaixian":["中等","属中等强度紫外线辐射天气，外出时建议涂擦SPF高于15、PA+的防晒护肤品，戴帽子、太阳镜。"],"ganmao":["少发","各项气象条件适宜，发生感冒机率较低。但请避免长期处于空调房间中，以防感冒。"],"xiche":["不宜","不宜洗车，未来24小时内有雨，如果在此期间洗车，雨水和路上的泥水可能会再次弄脏您的爱车。"],"wuran":null,"chuanyi":["炎热","天气炎热，建议着短衫、短裙、短裤、薄型T恤衫等清凉夏季服装。"]}},"weather":[{"date":"2017-08-26","week":"六","nongli":"七月初五","info":{"dawn":null,"day":["4","雷阵雨","36","","微风","06:02","出门记得带伞，行走驾驶做好防滑准备"],"night":["3","阵雨","26","东北风","3-4 级","18:45","出门记得带伞，行走驾驶做好防滑准备"]}},{"date":"2017-08-27","week":"日","nongli":"七月初六","info":{"dawn":["3","阵雨","26","东北风","3-4 级","18:45"],"day":["9","大雨","29","东风","5-6 级","06:02","出门记得带伞，行走驾驶做好防滑准备"],"night":["8","中雨","25","东风","3-4 级","18:44","出门记得带伞，行走驾驶做好防滑准备"]}},{"date":"2017-08-28","week":"一","nongli":"七月初七","info":{"dawn":["8","中雨","25","东风","3-4 级","18:44"],"day":["3","阵雨","33","东南风","3-4 级","06:02"],"night":["1","多云","24","东北风","3-4 级","18:44"]}},{"date":"2017-08-29","week":"二","nongli":"七月初八","info":{"dawn":["1","多云","24","东北风","3-4 级","18:44"],"day":["4","雷阵雨","31","南风","3-4 级","06:03"],"night":["1","多云","24","","微风","18:43"]}},{"date":"2017-08-30","week":"三","nongli":"七月初九","info":{"dawn":["1","多云","24","无持续风向","微风","18:43"],"day":["4","雷阵雨","34","","微风","06:03"],"night":["3","阵雨","25","","微风","18:42"]}},{"date":"2017-08-31","week":"四","nongli":"七月初十 ","info":{"dawn":null,"day":["4","雷阵雨","35","东北风","微风","07:30"],"night":["1","多云","26","东北风","微风","19:30"]}},{"date":"2017-09-01","week":"五","nongli":"七月十一","info":{"dawn":null,"day":["4","雷阵雨","33","东北风","微风","07:30"],"night":["3","阵雨","26","东北风","微风","19:30"]}}],"pm25":{"key":"Heyuan","show_desc":"0","pm25":{"curPm":"41","pm25":"11","pm10":"41","level":"1","quality":"优","des":"空气很好，可以外出活动"},"dateTime":"2017年08月26日18时","cityName":"河源"},"isForeign":0}
     * error_code : 0
     * reason : Succes
     */

    private int error_code;
    private String reason;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public static class ResultBean {
        /**
         * wind : {"windspeed":null,"direct":"西南风","power":"2级","offset":null}
         * time : 18:00:00
         * weather : {"humidity":"90","img":"3","info":"阵雨","temperature":"26"}
         * dataUptime : 1503743106
         * date : 2017-08-26
         * city_code : 101281201
         * city_name : 河源
         * week : 6
         * moon : 七月初五
         */

        private RealtimeBean realtime;
        /**
         * date : 2017-8-26
         * info : {"kongtiao":["部分时间开启","您将感到些燥热，建议您在适当的时候开启制冷空调来降低温度，以免中暑。"],"yundong":["较不宜","有降水，推荐您在室内进行低强度运动；若坚持户外运动，须注意携带雨具。"],"ziwaixian":["中等","属中等强度紫外线辐射天气，外出时建议涂擦SPF高于15、PA+的防晒护肤品，戴帽子、太阳镜。"],"ganmao":["少发","各项气象条件适宜，发生感冒机率较低。但请避免长期处于空调房间中，以防感冒。"],"xiche":["不宜","不宜洗车，未来24小时内有雨，如果在此期间洗车，雨水和路上的泥水可能会再次弄脏您的爱车。"],"wuran":null,"chuanyi":["炎热","天气炎热，建议着短衫、短裙、短裤、薄型T恤衫等清凉夏季服装。"]}
         */

        private LifeBean life;
        /**
         * key : Heyuan
         * show_desc : 0
         * pm25 : {"curPm":"41","pm25":"11","pm10":"41","level":"1","quality":"优","des":"空气很好，可以外出活动"}
         * dateTime : 2017年08月26日18时
         * cityName : 河源
         */

        private Pm25Bean pm25;
        private int isForeign;
        /**
         * date : 2017-08-26
         * week : 六
         * nongli : 七月初五
         * info : {"dawn":null,"day":["4","雷阵雨","36","","微风","06:02","出门记得带伞，行走驾驶做好防滑准备"],"night":["3","阵雨","26","东北风","3-4 级","18:45","出门记得带伞，行走驾驶做好防滑准备"]}
         */

        private List<WeatherBean> weather;

        public RealtimeBean getRealtime() {
            return realtime;
        }

        public void setRealtime(RealtimeBean realtime) {
            this.realtime = realtime;
        }

        public LifeBean getLife() {
            return life;
        }

        public void setLife(LifeBean life) {
            this.life = life;
        }

        public Pm25Bean getPm25() {
            return pm25;
        }

        public void setPm25(Pm25Bean pm25) {
            this.pm25 = pm25;
        }

        public int getIsForeign() {
            return isForeign;
        }

        public void setIsForeign(int isForeign) {
            this.isForeign = isForeign;
        }

        public List<WeatherBean> getWeather() {
            return weather;
        }

        public void setWeather(List<WeatherBean> weather) {
            this.weather = weather;
        }

        public static class RealtimeBean {
            /**
             * windspeed : null
             * direct : 西南风
             * power : 2级
             * offset : null
             */

            private WindBean wind;
            private String time;
            /**
             * humidity : 90
             * img : 3
             * info : 阵雨
             * temperature : 26
             */

            private WeatherBean weather;
            private String dataUptime;
            private String date;
            private String city_code;
            private String city_name;
            private String week;
            private String moon;

            public WindBean getWind() {
                return wind;
            }

            public void setWind(WindBean wind) {
                this.wind = wind;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public WeatherBean getWeather() {
                return weather;
            }

            public void setWeather(WeatherBean weather) {
                this.weather = weather;
            }

            public String getDataUptime() {
                return dataUptime;
            }

            public void setDataUptime(String dataUptime) {
                this.dataUptime = dataUptime;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getCity_code() {
                return city_code;
            }

            public void setCity_code(String city_code) {
                this.city_code = city_code;
            }

            public String getCity_name() {
                return city_name;
            }

            public void setCity_name(String city_name) {
                this.city_name = city_name;
            }

            public String getWeek() {
                return week;
            }

            public void setWeek(String week) {
                this.week = week;
            }

            public String getMoon() {
                return moon;
            }

            public void setMoon(String moon) {
                this.moon = moon;
            }

            public static class WindBean {
                private Object windspeed;
                private String direct;
                private String power;
                private Object offset;

                public Object getWindspeed() {
                    return windspeed;
                }

                public void setWindspeed(Object windspeed) {
                    this.windspeed = windspeed;
                }

                public String getDirect() {
                    return direct;
                }

                public void setDirect(String direct) {
                    this.direct = direct;
                }

                public String getPower() {
                    return power;
                }

                public void setPower(String power) {
                    this.power = power;
                }

                public Object getOffset() {
                    return offset;
                }

                public void setOffset(Object offset) {
                    this.offset = offset;
                }
            }

            public static class WeatherBean {
                private String humidity;
                private String img;
                private String info;
                private String temperature;

                public String getHumidity() {
                    return humidity;
                }

                public void setHumidity(String humidity) {
                    this.humidity = humidity;
                }

                public String getImg() {
                    return img;
                }

                public void setImg(String img) {
                    this.img = img;
                }

                public String getInfo() {
                    return info;
                }

                public void setInfo(String info) {
                    this.info = info;
                }

                public String getTemperature() {
                    return temperature;
                }

                public void setTemperature(String temperature) {
                    this.temperature = temperature;
                }
            }
        }

        public static class LifeBean {
            private String date;
            /**
             * kongtiao : ["部分时间开启","您将感到些燥热，建议您在适当的时候开启制冷空调来降低温度，以免中暑。"]
             * yundong : ["较不宜","有降水，推荐您在室内进行低强度运动；若坚持户外运动，须注意携带雨具。"]
             * ziwaixian : ["中等","属中等强度紫外线辐射天气，外出时建议涂擦SPF高于15、PA+的防晒护肤品，戴帽子、太阳镜。"]
             * ganmao : ["少发","各项气象条件适宜，发生感冒机率较低。但请避免长期处于空调房间中，以防感冒。"]
             * xiche : ["不宜","不宜洗车，未来24小时内有雨，如果在此期间洗车，雨水和路上的泥水可能会再次弄脏您的爱车。"]
             * wuran : null
             * chuanyi : ["炎热","天气炎热，建议着短衫、短裙、短裤、薄型T恤衫等清凉夏季服装。"]
             */

            private InfoBean info;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public InfoBean getInfo() {
                return info;
            }

            public void setInfo(InfoBean info) {
                this.info = info;
            }

            public static class InfoBean {
                private Object wuran;
                private List<String> kongtiao;
                private List<String> yundong;
                private List<String> ziwaixian;
                private List<String> ganmao;
                private List<String> xiche;
                private List<String> chuanyi;

                public Object getWuran() {
                    return wuran;
                }

                public void setWuran(Object wuran) {
                    this.wuran = wuran;
                }

                public List<String> getKongtiao() {
                    return kongtiao;
                }

                public void setKongtiao(List<String> kongtiao) {
                    this.kongtiao = kongtiao;
                }

                public List<String> getYundong() {
                    return yundong;
                }

                public void setYundong(List<String> yundong) {
                    this.yundong = yundong;
                }

                public List<String> getZiwaixian() {
                    return ziwaixian;
                }

                public void setZiwaixian(List<String> ziwaixian) {
                    this.ziwaixian = ziwaixian;
                }

                public List<String> getGanmao() {
                    return ganmao;
                }

                public void setGanmao(List<String> ganmao) {
                    this.ganmao = ganmao;
                }

                public List<String> getXiche() {
                    return xiche;
                }

                public void setXiche(List<String> xiche) {
                    this.xiche = xiche;
                }

                public List<String> getChuanyi() {
                    return chuanyi;
                }

                public void setChuanyi(List<String> chuanyi) {
                    this.chuanyi = chuanyi;
                }
            }
        }

        public static class Pm25Bean {
            private String key;
            private String show_desc;
            /**
             * curPm : 41
             * pm25 : 11
             * pm10 : 41
             * level : 1
             * quality : 优
             * des : 空气很好，可以外出活动
             */

            private Pm25Beans pm25;
            private String dateTime;
            private String cityName;

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }

            public String getShow_desc() {
                return show_desc;
            }

            public void setShow_desc(String show_desc) {
                this.show_desc = show_desc;
            }

            public Pm25Beans getPm25() {
                return pm25;
            }

            public void setPm25(Pm25Beans pm25) {
                this.pm25 = pm25;
            }

            public String getDateTime() {
                return dateTime;
            }

            public void setDateTime(String dateTime) {
                this.dateTime = dateTime;
            }

            public String getCityName() {
                return cityName;
            }

            public void setCityName(String cityName) {
                this.cityName = cityName;
            }

            public static class Pm25Beans {
                private String curPm;
                private String pm25;
                private String pm10;
                private String level;
                private String quality;
                private String des;

                public String getCurPm() {
                    return curPm;
                }

                public void setCurPm(String curPm) {
                    this.curPm = curPm;
                }

                public String getPm25() {
                    return pm25;
                }

                public void setPm25(String pm25) {
                    this.pm25 = pm25;
                }

                public String getPm10() {
                    return pm10;
                }

                public void setPm10(String pm10) {
                    this.pm10 = pm10;
                }

                public String getLevel() {
                    return level;
                }

                public void setLevel(String level) {
                    this.level = level;
                }

                public String getQuality() {
                    return quality;
                }

                public void setQuality(String quality) {
                    this.quality = quality;
                }

                public String getDes() {
                    return des;
                }

                public void setDes(String des) {
                    this.des = des;
                }
            }
        }

        public static class WeatherBean {
            private String date;
            private String week;
            private String nongli;
            /**
             * dawn : null
             * day : ["4","雷阵雨","36","","微风","06:02","出门记得带伞，行走驾驶做好防滑准备"]
             * night : ["3","阵雨","26","东北风","3-4 级","18:45","出门记得带伞，行走驾驶做好防滑准备"]
             */

            private InfoBean info;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getWeek() {
                return week;
            }

            public void setWeek(String week) {
                this.week = week;
            }

            public String getNongli() {
                return nongli;
            }

            public void setNongli(String nongli) {
                this.nongli = nongli;
            }

            public InfoBean getInfo() {
                return info;
            }

            public void setInfo(InfoBean info) {
                this.info = info;
            }

            public static class InfoBean {
                private Object dawn;
                private List<String> day;
                private List<String> night;

                public Object getDawn() {
                    return dawn;
                }

                public void setDawn(Object dawn) {
                    this.dawn = dawn;
                }

                public List<String> getDay() {
                    return day;
                }

                public void setDay(List<String> day) {
                    this.day = day;
                }

                public List<String> getNight() {
                    return night;
                }

                public void setNight(List<String> night) {
                    this.night = night;
                }
            }
        }
    }
}
