package Rendering

import scalafx.scene.paint.Color
import RayMath.Vector3D

class Material(
                val color: Vector3D = Vector3D(1,1,1),
                val emissionColor: Vector3D = Vector3D(0,0,0),
                val emissionStrength: Double = 0,
                val smoothness: Double = 0.0,
                val specularProbability: Double = 0.0,
                val specularColor: Vector3D = Vector3D(1,1,1),
              )
