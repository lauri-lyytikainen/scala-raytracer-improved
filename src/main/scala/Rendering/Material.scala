package Rendering

import RayMath.Vector3D

/**
 * A class that represents a material
 * @param color The color of the material
 * @param emissionColor The color of the emission
 * @param emissionStrength The strength of the emission
 * @param smoothness The smoothness of the material
 * @param specularProbability The probability of the material being specular
 * @param specularColor The color of the specular reflection
 */
class Material(
                val color: Vector3D = Vector3D(1,1,1),
                val emissionColor: Vector3D = Vector3D(0,0,0),
                val emissionStrength: Double = 0,
                val smoothness: Double = 0.0,
                val specularProbability: Double = 0.0,
                val specularColor: Vector3D = Vector3D(1,1,1),
              )
