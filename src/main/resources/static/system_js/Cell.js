import { HTMLFather as HTML } from "../system_js/HTMLFather.js";
import {CellFather} from "../system_js/Cell.min.js";
class Cell extends CellFather
{
    constructor(grid, data) {
        super(grid, data);
        this.createTools()
    }
    createElement(mdl)
    {
        this.inside = HTML.createAndAppend("DIV", this.getGrid().gridElem);
        if(mdl == null)
        {
            this.inside.style.gridArea = this.editGridArea();
            HTML.addStyles(["grid", "emptyCell", "zIndex50"], [this.inside]);
            let insideButton = HTML.createAndAppend("BUTTON", this.inside);
            insideButton.innerText = "Загрузить модуль";
            HTML.addStyles(["justifySelfCenter", "alignSelfCenter"], [insideButton]);
            insideButton.onclick = async ()=>{
                let req = await fetch("/js/list");
                let res = await req.json();
                console.log(res);
                this.popup = HTML.createPopup();
                let moduls = HTML.createAndAppend("DIV", this.popup);
                HTML.addStyles(["grid", "gapFivePix"], [moduls]);
                moduls.style.gridTemplateColumns = "repeat(3, auto)";
                res.forEach(arr => {
                    let b = HTML.createAndAppend("BUTTON", moduls);
                    b.innerText = arr[1];
                    b.dataset.id = arr[0];
                    b.dataset.grid = this.editGridArea();
                    b.onclick = async (e)=>{
                        this.moduleLink = arr[0];
                        await HTML.sendJSON(["pages","update_cells"], [this]);
                        this.loadModule(arr[0], e.target)};
                });
            }
        }
        else
        {
            this.moduleLink = mdl;
            this.loadModule(mdl)
        }
    }
    createTools()
    {
        this.tools = HTML.createAndAppend("DIV", this.getGrid().gridElem);
        HTML.addStyles(["grid", "gridAutoCols", "justifySelfEnd", "alignSelfEnd", "zIndex100", "gapFivePix", "padding5px", "pointer"], [this.tools]);
        let del = HTML.createAndAppend("DIV", this.tools);
        del.innerHTML = "<img src = '../static/images/del.png'/>";
        del.onclick = ()=>{
            this.getGrid().gridElem.removeChild(this.inside);
            this.getGrid().gridElem.removeChild(this.tools);
            this.getGrid().deleteCell(this); 
        };
        let add = HTML.createAndAppend("DIV", this.tools);
        add.innerHTML = "<img src = '../static/images/add.png'/>";
        add.onclick = ()=>{
            this.getGrid().addNextCell(this, this.getGrid());
        };
        /*
        let fullScreen = HTML.createAndAppend("DIV", this.tools);
        fullScreen.innerHTML = "<img src = '../static/images/fs.png'/>";
        function closeAct()
        {
            this.getGrid().gridElem.appendChild(this.inside);
            this.inside.style.gridArea = this.editGridArea();
        }
        fullScreen.onclick = ()=>{
            let pop = HTML.createPopup(this.inside, closeAct.bind(this));
            HTML.addStyles(["fullScreenPopup"],[pop]);           
        };*/
        HTML.addStyles(["oneGridTool", "justifySelfCenter", "alignSelfCenter"], [del, add])
        this.tools.style.gridArea = this.editGridArea();
    }
    async loadModule(moduleId)
    {
        let m = await import(`/js/get/${moduleId}`);
        let cls = new m.mdl();
        this.replaceElement(await cls.init(this.id));
        let tools = cls.getTools?.().toolsUnits;
        if(tools)
        {
            for(let t of tools)
            {
                this.tools.appendChild(t);
            }
        }
    }
    replaceElement(newElem)
    {
        this.getGrid().gridElem.replaceChild(newElem, this.inside);
        this.inside = newElem;
        this.inside.style.gridArea = this.editGridArea();
    }
    changePosition(newPosition)
    {
        this.number = newPosition + 1;
        this.inside.style.gridArea = this.editGridArea();
        this.tools.style.gridArea = this.editGridArea();
    }
}
export {Cell}