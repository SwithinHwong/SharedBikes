//创建地图
    var map = new AMap.Map('container', {
        zoom: 4
    });

    AMapUI.load(['ui/misc/PointSimplifier', 'lib/$'], function(PointSimplifier, $) {

        if (!PointSimplifier.supportCanvas) {
            alert('当前环境不支持 Canvas！');
            return;
        }

        var pointSimplifierIns = new PointSimplifier({
            map: map, //所属的地图实例

            getPosition: function(item) {

                if (!item) {
                    return null;
                }

                var parts = item.split(',');

                //返回经纬度
                return [parseFloat(parts[0]), parseFloat(parts[1])];
            },
            getHoverTitle: function(dataItem, idx) {
                return idx + ': ' + dataItem;
            },
            renderOptions: {
                //点的样式
                pointStyle: {
                    width: 6,
                    height: 6
                },
                //鼠标hover时的title信息
                hoverTitleStyle: {
                    position: 'top'
                }
            }
        });

        $('<div id="loadingTip">加载数据，请稍候...</div>').appendTo(document.body);
        $.get('http://a.amap.com/amap-ui/static/data/10w.txt', function(csv) {

            var data = csv.split('\n');

            pointSimplifierIns.setData(data);

            $('#loadingTip').remove();
        });





        pointSimplifierIns.on('pointClick pointMouseover pointMouseout', function(e, record) {
            console.log(e.type, record);
        });
    });