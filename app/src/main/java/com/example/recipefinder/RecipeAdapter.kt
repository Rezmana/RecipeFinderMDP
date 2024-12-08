package com.example.recipefinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipefinder.entities.Recipe
import com.squareup.picasso.Picasso

class RecipeAdapter(
    private var recipes: List<Recipe>,
    private val isManagingRecipes: Boolean, // Determines if the delete button is shown
    private val onDeleteClick: (Recipe) -> Unit, // Callback for delete action
    private val onItemClick: (Recipe) -> Unit // Callback for item click
) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recipe, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.bind(recipe)

        // Show or hide delete button based on isManagingRecipes
        if (isManagingRecipes) {
            holder.deleteButton.visibility = View.VISIBLE
            holder.deleteButton.setOnClickListener { onDeleteClick(recipe) }
        } else {
            holder.deleteButton.visibility = View.GONE
        }

        // Handle item click
        holder.itemView.setOnClickListener { onItemClick(recipe) }
    }

    override fun getItemCount(): Int = recipes.size

    fun updateRecipes(newRecipes: List<Recipe>) {
//        recipes.clear()
//        recipes.addAll(newRecipes)
        this.recipes = newRecipes
        notifyDataSetChanged()
    }

    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val recipeImage: ImageView = itemView.findViewById(R.id.iv_recipe_image)
        private val recipeName: TextView = itemView.findViewById(R.id.tv_recipe_name)
        private val cookingTime: TextView = itemView.findViewById(R.id.tv_recipe_cooking_time)
        val deleteButton: Button = itemView.findViewById(R.id.btn_delete_recipe)

        fun bind(recipe: Recipe) {
            recipeName.text = recipe.recipeName
            cookingTime.text = "${recipe.cookingTime ?: "N/A"} mins"

            // Load recipe image
            if (!recipe.imageUri.isNullOrEmpty()) {
                Picasso.get().load(recipe.imageUri).into(recipeImage)
            } else {
                recipeImage.setImageResource(R.drawable.background_placeholder)
            }
        }
    }

}


