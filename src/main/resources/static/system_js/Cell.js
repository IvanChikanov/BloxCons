import { HTMLFather as HTML } from "../system_js/HTMLFather.js";
import {CellFather} from "../system_js/Cell.min.js";
class Cell extends CellFather
{
    constructor(grid, data) {
        super(grid, data);
        this.createTools()
    }
    /*
    createElement()
    {
        this.inside = HTML.createAndAppend("DIV", this.getGrid().gridElem);
        if(this.moduleLink == null)
        {
            this.inside.style.gridArea = this.editGridArea();
            HTML.addStyles(["grid", "emptyCell", "zIndex50"], [this.inside]);
            let insideButton = HTML.createAndAppend("BUTTON", this.inside);
            insideButton.innerText = "Загрузить модуль";
            HTML.addStyles(["justifySelfCenter", "alignSelfCenter"], [insideButton]);
            console.log(this.getGrid().cellCount);
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
            this.loadModule(this.getModulePath(this.moduleLink));
        }
    }
    */
    createTools()
    {
        this.tools = HTML.createAndAppend("DIV", this.getGrid().gridElem);
        HTML.addStyles(["grid", "gridAutoCols", "justifySelfEnd", "alignSelfEnd", "zIndex600", "gapFivePix", "padding5px", "pointer"], [this.tools]);
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
        HTML.addStyles(["oneGridTool", "justifySelfCenter", "alignSelfCenter"], [del, add])
        this.tools.style.gridArea = this.editGridArea();
    }
    async loadModule(path)
    {
        let cls = await super.loadModule(path);
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
    getModulePath(moduleId)
    {
        if(moduleId == null)
        {
            return null;
        }
        return `/js/get/${moduleId}`;
    }
    getEmpty(cellGrid, cellInstance)
    {
        let obj =  {
            test: cellGrid,
            init: async function(){
                let insideParent = HTML.create("DIV");
                HTML.addStyles(["grid", "emptyCell", "zIndex50"], [insideParent]);
                let insideButton = HTML.createAndAppend("BUTTON", insideParent);
                insideButton.innerText = "Загрузить модуль";
                HTML.addStyles(["justifySelfCenter", "alignSelfCenter"], [insideButton]);
                insideButton.onclick = async ()=>{
                    let req = await fetch("/js/list");
                    let res = await req.json();
                    cellInstance.popup = HTML.createPopup();
                    let moduls = HTML.createAndAppend("DIV", cellInstance.popup);
                    HTML.addStyles(["grid", "gapFivePix"], [moduls]);
                    moduls.style.gridTemplateColumns = "repeat(3, auto)";
                    res.forEach(arr => {
                        let b = HTML.createAndAppend("BUTTON", moduls);
                        b.innerText = arr[1];
                        b.dataset.id = arr[0];
                        b.dataset.grid = cellInstance.editGridArea();
                        b.onclick = async (e)=>{
                            cellInstance.moduleLink = arr[0];
                            await HTML.sendJSON(["pages","update_cells"], [cellInstance]);
                            cellInstance.loadModule(cellInstance.getModulePath(cellInstance.moduleLink))};
                    });}
                let mobs = new MutationObserver(follow => {
                    cellGrid.createCell();
                    mobs.disconnect();
                });
                mobs.observe(cellGrid.gridElem, {childList: true});
                return insideParent;
            },
            needWaiting: false
        };
        obj.test = cellGrid;
        return obj;
    }
}
export {Cell}