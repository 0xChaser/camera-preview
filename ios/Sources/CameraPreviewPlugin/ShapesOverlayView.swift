import UIKit

class ShapesOverlayView: UIView {
    var shapes: [UIView] = []

    override init(frame: CGRect) {
        super.init(frame: frame)
        self.backgroundColor = .clear
        self.isUserInteractionEnabled = true
        self.isMultipleTouchEnabled = true
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    func addShape(type: String, color: UIColor) {
        let shapeView = UIView(frame: CGRect(x: 100, y: 100, width: 100, height: 100))
        shapeView.backgroundColor = color
        
        let panGesture = UIPanGestureRecognizer(target: self, action: #selector(handlePan(_:)))
        shapeView.addGestureRecognizer(panGesture)
        shapeView.isUserInteractionEnabled = true
        
        self.addSubview(shapeView)
        shapes.append(shapeView)
    }
    
    @objc func handlePan(_ gesture: UIPanGestureRecognizer) {
        guard let shapeView = gesture.view else { return }
        let translation = gesture.translation(in: self)
        
        shapeView.center = CGPoint(x: shapeView.center.x + translation.x, y: shapeView.center.y + translation.y)
        
        gesture.setTranslation(.zero, in: self)
        
        if gesture.state == .began {
            self.bringSubviewToFront(shapeView)
        }
    }
    
    func removeAllShapes() {
        for shape in shapes {
            shape.removeFromSuperview()
        }
        shapes.removeAll()
    }
}
